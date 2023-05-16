package com.nashtech.automation.setup;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.an.automation.excelhelper.ExcelHelper;
import com.an.automation.report.Log;

public class WebTestSetup extends WebTestBaseSetup {

	public static String dataFilePath;
	public static String sheetName;
	
	
	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

	@BeforeSuite
	public void beforeSuite() throws Exception {
		super.beforeSuite();
	}
	
	@BeforeTest
	@Parameters({ "browserName", "browserVersion", "platformName", "platformVersion" })
	public void beforeTest(@Optional("") String browserName, @Optional("") String browserVersion,
			@Optional("") String platformName, @Optional("") String platformVersion) throws Exception {
		super.beforeTest(browserName, browserVersion, platformName, platformVersion);
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		super.beforeClass();
		Log.startTestCase(this.getClass().getName());
	}

	@BeforeMethod
	@Parameters({ "browserName", "browserVersion", "platformName", "platformVersion" })
	public void beforeMethod(@Optional("") String browserName, @Optional("") String browserVersion,
			@Optional("") String platformName, @Optional("") String platformVersion, Method method,
			Object[] listParameter) throws Exception {
		String methodName = listParameter.length != 0 ? method.getName() + " - " + ArrayUtils.toString(listParameter) : method.getName();
		Log.info("+++++++++");
		Log.info("+++++++++");
		Log.info("+++++++++ Start testing: " + methodName + " ++++++++++++++");
		
		super.beforeMethod(browserName,browserVersion,platformName,platformVersion,method,listParameter);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Exception {
		super.afterMethod(result);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
		Log.endTestCase(this.getClass().getName());
		super.afterClass();
	}
	
	@AfterTest(alwaysRun = true)
	public void afterTest() throws Exception {
		super.afterTest();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		super.afterSuite();
	}
}
