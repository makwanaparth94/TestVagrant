package com.test.utilities;

import com.relevantcodes.extentreports.LogStatus;
import com.test.testbase.TestBase;

import io.restassured.response.Response;

public class ExtentMethods extends TestBase{
	
	/*-----------------------------------------For GET request------------------------------------------------------*/

	public static void Extentlogs_GET_WithoutParam(Response response, String Endpoint, String comment) 
	{
		test.log(LogStatus.INFO, "API Test Info : " + comment);
		test.log(LogStatus.INFO, "Endpoint is : " +Endpoint);
		test.log(LogStatus.INFO, "Response is : " +response.asString());
	}

	public static void Extentlogs_Error(String error) {
		test.log(LogStatus.INFO, "Failure Error : \n"+error);		
	}
}
