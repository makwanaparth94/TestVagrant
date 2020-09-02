package com.test.listeners;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.relevantcodes.extentreports.LogStatus;
import com.test.testbase.TestBase;
import com.test.utilities.Utilities;

public class CustomListeners extends TestBase implements ITestListener{

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		test = report.startTest(result.getName().toUpperCase());
		
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		test.log(LogStatus.PASS, result.getName().toUpperCase()+" PASS");
		report.endTest(test);
		report.flush();
	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		System.setProperty("org.uncommons.reportng.escape-output","false");
		try {
			Utilities.captureScreenshot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.log(LogStatus.FAIL, result.getName().toUpperCase()+" Failed with exception : "+result.getThrowable());
		test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.screenshotName));
		
		Reporter.log("Click to see Screenshot");
		Reporter.log("<a target=\"_blank\" href="+Utilities.screenshotName+">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href="+Utilities.screenshotName+"><img src="+Utilities.screenshotName+" height=200 width=200></img></a>");
		report.endTest(test);
		report.flush();
	}

	public void onTestSkipped(ITestResult result) {
		test.log(LogStatus.SKIP, result.getName().toUpperCase()+" Skipped");
		report.endTest(test);
		report.flush();
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

}
