## Table of contents
* [Project info](#project-info)
* [Technologies](#technologies)
* [Required Key Points before Execution](#required-key-points-before-execution)
* [Setup](#setup)

## Project info
This Project is to check whether Humidity & Temperature data matches to defined validator range by applying comparator on values what we get at runtime from both UI and BE
	
## Technologies
Data Driven Framework is created with:
* Selenium,TestNG & Core JAVA
* Maven for Project Management build 
* Extent Reports for rich reporting mechanism

## Required Key Points before Execution
* OR.properties is used to store locators and env.properties is used for Test Data.
	* To Change City value: From OR.properties file, Add city name to validateseachedText_CSS & hoverOnSelectedCity keys and From env.properties file, Put the same city name to searchTextValue key
* To Set up validator range for Temperature, Use 'setup_TempDiffInCelsius' key in env.properties file 
* To Set up validator range for Humidity, Use 'setup_HumidityDiffInPercentage' key in env.properties file 
	
## Setup
To run this project, pull it in local:

```
$ cd TestVagrant
$ mvn clean install
```
