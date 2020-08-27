package com.test.testcases;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.test.services.APIServices;
import com.test.testbase.TestBase;
import com.test.testbase.TestConfiguration;

import io.restassured.response.Response;

public class WeatherRangeTest extends TestBase{

	
	TestConfiguration testConf = new TestConfiguration();
	APIServices service = new APIServices();
	Response response = null;
	
	/*
	 * Before Test ---> Open Browser and URL
	 */
	@BeforeTest
	public void setUp() throws Exception
	{
		openBrowser();
		openURL() ;
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(or_getproperty("implicit.wait")), TimeUnit.SECONDS);
	}
	

	/*
	 * Wait till Alert doesn't handle then looks for Weather tab and Peform tab Operation
	 */
	@Test(priority = 1)
	public void openWeather() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement alertElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(or_getproperty("cancelAlert_CSS"))));
		alertElement.click();
		click(or_getproperty("openExtraMenuBars_CSS"));
		click(or_getproperty("weatherTab_CSS"));
	}
	
	/*
	 * Verify the Weather Page validation
	 */
	@Test(priority =2)
	public void verifyWeatherPage() {
		if(isElementVisible(or_getproperty("validateTextOnWeatherPage"))) {
			Assert.assertTrue("Successfully landed on Weather Page",true);
		}else {
			Assert.assertTrue("Something went wrong on validation of Weather Page",false);
		}
	}
	
	/*
	 *  Search for the city and Validate Humidity (% Unit) & Temp (C Unit) from BE and PASS/Fail based on Validator Range
	 */
	@Test(priority = 3)
	public void locateCity() throws InterruptedException {
		//Search for the CITY and Select a checkbox if it is not selected
		type(or_getproperty("seachTextBox"),or_getproperty("searchTextValue"));
		try{
			if(isElementVisible(or_getproperty("validateseachedText_CSS"))) {	
		
				if(!(isAlreadySelected(or_getproperty("validateseachedText_CSS")))){
					click(or_getproperty("validateseachedText_CSS"));
					Thread.sleep(5000);
					Assert.assertTrue(getElementByXpathContainsText("//*[contains(@title,'"+ or_getproperty("searchTextValue")+"')]").isDisplayed());	
				}
				
			getElementByXpathContainsText("//*[contains(@title,'"+ or_getproperty("searchTextValue")+"')]").click();
			//Target to desired city location and takes Humidity and Temperature data 
			Actions action = new Actions(driver);
			action.moveToElement(getElementByXpathContainsText("//*[contains(@title,'"+ or_getproperty("searchTextValue")+"')]")).build().perform();
			
			WebElement ui_temp = getElementByXpathContainsText(or_getproperty("tempForSelecetedCity_XPATH"));
			int ui_temp_data = getDecimalfromHumidity(ui_temp.getText());
			
			WebElement ui_humidity = getElementByXpathContainsText(or_getproperty("humidityForSelectedCity_XPATH"));
			int ui_humidity_data = getDecimalfromHumidity(ui_humidity.getText());
			Assert.assertNotEquals(ui_temp.getText(), "");

			//Get the HUmidity and Temp data from Rest API
			response = service.getCityWeatherData();
			System.out.println(response.asString());
			if(response.getStatusCode() == 200) {
			  
				int api_humidity_data = Integer.parseInt(response.jsonPath().get("main.humidity").toString()); 
				float api_temp_data = Float.parseFloat((response.jsonPath().get("main.temp").toString()));
			 
				//Call to comparator to check the difference
				int actual_humidity_difference = validateHumidityDiff(ui_humidity_data,api_humidity_data); 
				int actual_temp_difference = (int)validateTempDiff(ui_temp_data, convertTemptoCelsius(api_temp_data));

				//Validate the  differece wrt given Validator range
				try{
					int expected_humidity_difference = Integer.parseInt(testConf.setup_HumidityDiffInPercentage()); 
					int expected_temp_difference = Integer.parseInt(testConf.setup_TempDiffInCelsius());
					  
					  if(actual_humidity_difference <= expected_humidity_difference && actual_temp_difference<= expected_temp_difference) {
						  Assert.assertTrue("Difference is in specified range", true); 
					  } else{ 
						  Assert.assertTrue("Either Humidity or Temp OR both parameters condition is not matched to required range", false); 
					  } 
				}catch(NumberFormatException e) {
					throw e;
				}
			  } else {
				  throw new AssertionError(); 
			}
			 		  						
			}
		}catch(NoSuchElementException e) {
			Assert.assertTrue("Element on Weather page is not displayed", false);
		}
	}
	
	@AfterTest
	public void closeUp() throws Exception
	{
		closeBrowser();
	}
}
