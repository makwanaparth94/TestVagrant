package com.test.testcases;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;
import com.relevantcodes.extentreports.LogStatus;
import com.test.services.APIServices;
import com.test.testbase.Endpoint;
import com.test.testbase.TestBase;
import com.test.testbase.TestConfiguration;
import com.test.utilities.ExtentMethods;
import com.test.utilities.Utilities;

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
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(env_getproperty("implicit.wait")), TimeUnit.SECONDS);
	}
	

	/*
	 * Wait till Alert doesn't handle then looks for Weather tab and Peform tab Operation
	 */
	@Test(priority = 1)
	public void openWeather() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement alertElement = wait.until(ExpectedConditions.elementToBeClickable(waitforElementCondition(or_getproperty("cancelAlert_CSS"))));
		alertElement.click();
		test.log(LogStatus.INFO, "Waiting for an alert to visible and handle it");
		
		click(or_getproperty("openExtraMenuBars_CSS"));
		
		click(or_getproperty("weatherTab_CSS"));
		test.log(LogStatus.INFO, "Click on 'Weather' tab");
	}
	
	/*
	 * Verify the Weather Page validation
	 */
	@Test(priority =2)
	public void verifyWeatherPage() {
		if(!isElementVisible(or_getproperty("validateTextOnWeatherPage"))) {
			Assert.fail("Something went wrong on validating text on Weather Page");
		}
		test.log(LogStatus.INFO, "Validating the text on weather page");
	}
	
	/*
	 *  Search for the city and Validate Humidity (% Unit) & Temp (C Unit) from BE and PASS/Fail based on Validator Range
	 */
	@Test(priority = 3)
	public void locateCity() throws Exception {
		//Search for the CITY and Select a check box if it is not selected
		type(or_getproperty("seachTextBox"),testConf.getCityName());
		test.log(LogStatus.INFO, "Type "+ testConf.getCityName() + "city in Pin Your City ");
	
		try {
			if(isElementVisible(or_getproperty("validateseachedText_CSS"))) {	
				
				if(!(isAlreadySelected(or_getproperty("validateseachedText_CSS")))){
					click(or_getproperty("validateseachedText_CSS"));
					test.log(LogStatus.INFO, "Select "+ or_getproperty("validateTextOnWeatherPage")+ " city as It is not selected");
					Thread.sleep(5000);
					Assert.assertTrue(getElementByXpathContainsText(or_getproperty("hoverOnSelectedCity")).isDisplayed());	
				}
				
			getElementByXpathContainsText(or_getproperty("hoverOnSelectedCity")).click();
			
			//Target location to desired city and takes Humidity and Temperature data 
			Actions action = new Actions(driver);
			action.moveToElement(getElementByXpathContainsText(or_getproperty("hoverOnSelectedCity"))).build().perform();
			test.log(LogStatus.INFO, "Hover to searched city on map and take temp and humidity data for validator range with API data");
			
			WebElement ui_temp = getElementByXpathContainsText(or_getproperty("tempForSelecetedCity_XPATH"));
			int ui_temp_data = Utilities.getDecimalfromHumidity(ui_temp.getText());
			
			WebElement ui_humidity = getElementByXpathContainsText(or_getproperty("humidityForSelectedCity_XPATH"));
			int ui_humidity_data = Utilities.getDecimalfromHumidity(ui_humidity.getText());
			Assert.assertNotEquals(ui_temp.getText(), "");
			
			//Get the HUmidity and Temp data from Rest API
			response = service.getCityWeatherData();
			//Logging Extent
			ExtentMethods.Extentlogs_GET_WithoutParam(response, Endpoint.BY_CITY_NAME, "Get the details of weather for " +testConf.getCityName()+ " & Validate it with UI data by setting up Temp range: "+testConf.setup_TempDiffInCelsius() +" &Humidity range: "+testConf.setup_HumidityDiffInPercentage());
			
			if(response.getStatusCode() == 200) {
			  
				int api_humidity_data = Integer.parseInt(response.jsonPath().get("main.humidity").toString()); 
				float api_temp_data = Float.parseFloat((response.jsonPath().get("main.temp").toString()));
			 
				//Call to comparator to check the difference
				int actual_humidity_difference = Utilities.validateHumidityDiff(ui_humidity_data,api_humidity_data); 
				int actual_temp_difference = (int)Utilities.validateTempDiff(ui_temp_data, Utilities.convertTemptoCelsius(api_temp_data));

				//Validate the  differece wrt given Validator range
				try{
					int expected_humidity_difference = Integer.parseInt(testConf.setup_HumidityDiffInPercentage()); 
					int expected_temp_difference = Integer.parseInt(testConf.setup_TempDiffInCelsius());
					  
					  if(actual_humidity_difference <= expected_humidity_difference && actual_temp_difference<= expected_temp_difference) {
						  test.log(LogStatus.INFO,"Temp and Humidity data are in validator range.Therefore, Test is passed");
					  } else{ 
						  Assert.fail("Either Humidity or Temp OR both parameters condition is not matched to required range"); 
					  } 
				}catch(NumberFormatException e) {
					ExtentMethods.Extentlogs_Error("Getting an issue with casting numbers");
					throw e;
				}
				
			  }else {
				  ExtentMethods.Extentlogs_Error("Getting an error because GET API isn't giving 2xx response code");
				  throw new AssertionError();
			  }
			
			}
			else {
				Assert.fail("Element on Weather page is not displayed");
			}
		}catch(Exception e) {
			ExtentMethods.Extentlogs_Error(e.getMessage());
			throw e;
		}
	}
	
	@AfterTest
	public void closeUp() throws Exception
	{
		closeBrowser();
	}
}
