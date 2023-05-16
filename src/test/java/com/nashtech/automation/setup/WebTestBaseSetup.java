package com.nashtech.automation.setup;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
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

import com.an.automation.report.HtmlReporter;
import com.an.automation.selenium.ExtendedWebDriver;
import com.an.automation.selenium.WebDriverCreator;
import com.an.automation.selenium.WebDriverManager_;
import com.an.automation.utility.FilePaths;

public class WebTestBaseSetup {

	private static EnvironmentVariables envVars;
	public ExtendedWebDriver currentPage;
	public EnvironmentVariables getEnvironmentVariables() {
		return envVars;
	}
	
	public WebDriver getDriver() {
		return WebDriverManager_.getDriver();
	}

	@BeforeSuite
	public void beforeSuite() throws Exception {
		// Init Report Directory
		FilePaths.initReportDirectory();
	}

	@BeforeTest
	@Parameters({ "browserName", "browserVersion", "platformName", "platformVersion" })
	public void beforeTest(@Optional("") String browserName, @Optional("") String browserVersion,
			@Optional("") String platformName, @Optional("") String platformVersion) throws Exception {
		// Create html report by platform, device name and os version
		String reportFilePath = FilePaths.getReportFolder() + File.separator + "Report_" + browserName.toUpperCase()
				+ "_" + platformName + "_" + platformVersion + ".html";
		HtmlReporter.setReporter(reportFilePath);
		
		HtmlReporter.currentTest = "Test Setup";
		HtmlReporter.createTest("Test Setup", "");
		// start new driver instance by device name and os version
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		HtmlReporter.currentTest = this.getClass().getSimpleName();
		HtmlReporter.createTest(this.getClass().getSimpleName(), "");
	}

	@BeforeMethod
	@Parameters({ "browserName", "browserVersion", "platformName", "platformVersion" })
	public void beforeMethod(@Optional("") String browserName, @Optional("") String browserVersion,
			@Optional("") String platformName, @Optional("") String platformVersion, Method method,
			Object[] listParameter) throws Exception {
		String testParameters = "";
		for(Object param : listParameter) {
			testParameters += " - [" + param.toString() + "]";
		}
		String methodName = method.getName() + testParameters;
		HtmlReporter.createNode(this.getClass().getSimpleName(), methodName, "");

		WebDriverManager_.initDriver(browserName, browserVersion, platformName, platformVersion, methodName);
		currentPage = new ExtendedWebDriver(getDriver());
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Exception {
		String mess = "";
		
		try {
			switch (result.getStatus()) {
			case ITestResult.SUCCESS:
				mess = String.format("The test [%s] is PASSED", result.getName());
				currentPage.markTestStatus("passed",mess);
				HtmlReporter.pass(mess,currentPage.takeScreenshot());
				break;
			case ITestResult.SKIP:
				mess = String.format("The test [%s] is SKIPPED - n%s", result.getName(),result.getThrowable().getMessage());
				currentPage.markTestStatus("failed",String.format("The test [%s] is SKIPPED - %s",result.getName(),result.getThrowable().getMessage().substring(0,15)));
				HtmlReporter.skip(mess, result.getThrowable());
				break;
			case ITestResult.FAILURE:
				mess = String.format("The test [%s] is FAILED - %s", result.getName(),result.getThrowable().getMessage());
				currentPage.markTestStatus("failed",String.format("The test [%s] is FAILED - %s",result.getName(),result.getThrowable().getMessage().substring(0,15)));
				HtmlReporter.fail(mess, result.getThrowable(), currentPage.takeScreenshot());
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//Thread.sleep(10000);
			getDriver().quit();
		}

	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@AfterTest(alwaysRun = true)
	public void afterTest() throws Exception {
		HtmlReporter.flush();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
	}
}
