package com.an.automation.selenium;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;


public class WebDriverManager_ {
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	
	public static void initDriver(String browser,String browserVersion,String platformName,String platformVersion,String methodName) throws FileNotFoundException, IOException {
		if (getDriver() == null) {
			try {
				WebDriverCreator creator = new WebDriverCreator(browser,browserVersion,platformName,platformVersion);
				creator.setTestSessionName(methodName);
				creator.startDriver();
				creator.getBaseDriver();
				driver.set(creator.getBaseDriver());
			} catch (Exception exception) {
				exception.printStackTrace();
				throw new SkipException("Cannot initiator driver!", exception);
			}
		}
		
		
	}

	public static WebDriver getDriver() {
		return driver.get();
	}
}
