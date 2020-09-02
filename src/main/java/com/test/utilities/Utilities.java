package com.test.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.test.testbase.TestBase;

public class Utilities extends TestBase{

	
	public static String screenshotPath;
	public static String screenshotName;

	public static void captureScreenshot() throws IOException {

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		FileUtils.copyFile(scrFile,
				new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + screenshotName));

	}
	
	

	/*
	 * Code to take Difference on Humidity and Temperature
	 */
	public static int validateHumidityDiff(int ui_data,int api_data) {
		
		if(ui_data > api_data) {
			difference = ui_data - api_data;
		}else {
			difference = api_data - ui_data;
		}
		return difference;
	}
	
	public static float validateTempDiff(float ui_data,float api_data) {
		
		if(ui_data > convertTemptoCelsius(api_data)) {
			difference = (int) (ui_data - api_data);
		}else {
			difference = (int) (api_data - ui_data);
		}
		return difference;
	}
	
	//Code to get only INT value as all dependent operations are performed in percentage UNIT
	public static int getDecimalfromHumidity(String text) {
		String[] arrSplit = text.split(":");
		String[] decimalNo ;
		if(arrSplit[1].split("%") != null) {
			 decimalNo = arrSplit[1].split("%");
				return Integer.parseInt(decimalNo[0].trim());
		}
		return Integer.parseInt(arrSplit[1].trim());
	}
	
	//Code to convert Temperature from KelVin to Celsius 
	public static float convertTemptoCelsius(float tempInKelvin) {
		return (float) (tempInKelvin - 273.15);
	}
	
	
}
