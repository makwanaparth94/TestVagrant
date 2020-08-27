package com.test.services;

import com.test.testbase.Endpoint;
import com.test.testbase.TestConfiguration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class APIServices {

	TestConfiguration testConfig = new TestConfiguration();
	
	public Response getCityWeatherData() {
		
		Response response = null;
		RestAssured.baseURI = testConfig.getBaseURI();
		//RequestSpecification request = RestAssured.given();
		try {
			response =given()
					.queryParam("q", testConfig.getCityName()) 
                    .queryParam("appid", testConfig.getAppID()) 
                    .log().all()
                    .when()
                    .get(Endpoint.BY_CITY_NAME);
		}catch(Exception e) {
			
		}
		return response;
		
	}
	
}
