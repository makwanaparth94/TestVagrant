package com.test.testbase;

public class TestConfiguration extends TestBase{

	public String getCityName() {
		String cityName = or_getproperty("searchTextValue");
		return cityName;
	}
	
	public String getAppID() {
		String appID = or_getproperty("appID");
		return appID;
	}
	
	public String getBaseURI() {
		String appID = or_getproperty("base_URI");
		return appID;
	}
	
	public String setup_HumidityDiffInPercentage() {
		String setup_HumidityDiff = or_getproperty("setup_HumidityDiffInPercentage");
		return setup_HumidityDiff;
	}
	
	public String setup_TempDiffInCelsius() {
		String setup_TempDiffInCelsius = or_getproperty("setup_TempDiffInCelsius");
		return setup_TempDiffInCelsius;
	}
}
