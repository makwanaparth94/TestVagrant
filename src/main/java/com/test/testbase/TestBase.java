package com.test.testbase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.TIMEOUT;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.test.utilities.ExtentReporter;

public class TestBase {


	public static WebDriver driver = null;
	public static Properties OR = null;
	public static boolean isInitalized = false;
	public static boolean isBrowserOpened = false;
	public static boolean isURLOpened = false;
	public static int difference;
	public ExtentReports report = ExtentReporter.getInstance();
	
	public static ExtentTest test;
	// Code to open Browser
	public void openBrowser() {
		if (!isBrowserOpened) {
			if (config_getproperty("browserType").equals("CHROME")){
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
				driver = new ChromeDriver();
			}
	
			isBrowserOpened = true; 
			driver.manage().window().maximize();
		}	
	}
	
	// Code to hit URL in opened browser
	public boolean openURL() {
		driver.get(config_getproperty("cfURL"));
		
		isURLOpened = true;
		return isURLOpened;
	}
	
	// Code to close Browser
	public void closeBrowser() {
		driver.quit();
		isBrowserOpened = false;
	}
	
	// Code to load configuration properity file
	public static String config_getproperty(String propertyname) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//configurations//config.properties");
			// load a properties file
			prop.load(input);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		// Return the property value
			return prop.getProperty(propertyname);
		}
		
	// Code to load OR property file
	public static String or_getproperty(String propertyname) {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//configurations//OR.properties");
			// load a properties file
			prop.load(input);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Return the property value
		return prop.getProperty(propertyname);
		}
		
		//Code to Click on Element by using CSS SELECTOR only
		public void click(String locator) {
			driver.findElement(By.cssSelector(locator)).click();
		}

		//Code to validate visibility of an Element by using CSS SELECTOR only
		public boolean isElementVisible(String propertyname) {
			
			if(driver.findElement(By.cssSelector(propertyname)).isDisplayed()) {
				return true;
			}
			return false;
		}

		//Code to type in text box by using CSS SELECTOR only
		public void type(String propertyname,String value) {
			driver.findElement(By.cssSelector(propertyname)).sendKeys(value);
		}

		//Code to validate Element is selected or not by using CSS SELECTOR only
		public boolean isAlreadySelected(String propertyname) {
			if(driver.findElement(By.cssSelector(propertyname)).isSelected()) {
				return true;
			}
			return false;
		}

		//Code to validate Text Contains by using XPATH only
		public WebElement getElementByXpathContainsText(String xpathExpression)
		{
			return driver.findElement(By.xpath(xpathExpression));
	    }
	
		//Code to validate condition for elementToBeClickable
		public By waitforElementCondition(String cssExpression) {
			return By.cssSelector(cssExpression);
		}
		
		
}
