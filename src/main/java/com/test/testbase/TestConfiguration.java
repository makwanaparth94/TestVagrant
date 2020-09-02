package com.test.testbase;

public class TestConfiguration extends TestBase{

	public String getCityName() {
		String cityName = env_getproperty("searchTextValue");
		return cityName;
	}
	
	public String getAppID() {
		String appID = env_getproperty("appID");
		return appID;
	}
	
	public String getBaseURI() {
		String appID = env_getproperty("base_URI");
		return appID;
	}
	
	public String setup_HumidityDiffInPercentage() {
		String setup_HumidityDiff = env_getproperty("setup_HumidityDiffInPercentage");
		return setup_HumidityDiff;
	}
	
	public String setup_TempDiffInCelsius() {
		String setup_TempDiffInCelsius = env_getproperty("setup_TempDiffInCelsius");
		return setup_TempDiffInCelsius;
	}
}
