package com.an.automation.selenium;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.an.automation.report.HtmlReporter;
import com.an.automation.report.Log;
import com.an.automation.utility.FilePaths;
import com.an.automation.utility.PropertiesLoader;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverCreator {

	public static final int IMPLICIT_WAIT_TIME = 10;
	private Properties framework_configuration;
	private Properties selenium_configuration;
	
	// browserstack/local;
	private String test_location;
	
	private String browserName;
	private String browserVersion;
	protected String platformName;
	private String platformVersion;
	private String screenResolution;
	
	//use for set browserstack session name
	private String test_session_name;
	
	//use for set browserstack build name
	private String test_build_name;
	
	public WebDriverCreator() throws Exception {
		this("","","","","");
	}
	
	public WebDriverCreator(String browserName) throws Exception {
		this(browserName,"","","","");
	}
	
	public WebDriverCreator(String browserName,String platformName) throws Exception {
		this(browserName,"",platformName,"","");
	}
	
	public WebDriverCreator(String browserName,String platformName,String platformVersion) throws Exception {
		this(browserName,"",platformName,platformVersion,"");
	}
	
	public WebDriverCreator(String browserName,String browserVersion,String platformName,String platformVersion) throws Exception {
		this(browserName,browserVersion,platformName,platformVersion,"");
	}
	
	public WebDriverCreator(String browserName,String browserVersion, String platformName, String platformVersion, String screenResolution) throws Exception {
		framework_configuration = PropertiesLoader.getPropertiesLoader().framework_configuration;
		selenium_configuration = PropertiesLoader.getPropertiesLoader().selenium_configuration;
		
		if(browserName.equalsIgnoreCase("default") || browserName.equalsIgnoreCase("")) {
			browserName = selenium_configuration.getProperty("selenium.browser");
		}
		if(browserVersion.equalsIgnoreCase("default") || browserVersion.equalsIgnoreCase("")) {
			browserVersion = selenium_configuration.getProperty("selenium.browserVersion");
		}
		if(platformName.equalsIgnoreCase("default") || platformName.equalsIgnoreCase("")) {
			platformName = selenium_configuration.getProperty("selenium.os");
		}
		if(platformVersion.equalsIgnoreCase("default") || platformVersion.equalsIgnoreCase("")) {
			platformVersion = selenium_configuration.getProperty("selenium.osVersion");
		}
		if(screenResolution.equalsIgnoreCase("default") || screenResolution.equalsIgnoreCase("")) {
			screenResolution = selenium_configuration.getProperty("selenium.screen.resolution");
		}	
		
		this.browserName = browserName;
		this.browserVersion = browserVersion;
		this.platformName = platformName;
		this.platformVersion = platformVersion;
		this.screenResolution = screenResolution;
	} 
	
	public void setTestLocation(String location) {
		this.test_location = location;
	}
	
	public String getTestLocation() {
		return test_location;
	}
	
	
	
	public String getTestSessionName() {
		if(test_session_name == null || test_session_name.equals("")) {
			test_session_name = selenium_configuration.getProperty("browserstack.name");
		}
		return test_session_name;
	}

	public void setTestSessionName(String test_session_name) {
		this.test_session_name = test_session_name;
	}

	public String getTestBuildName() {
		if(test_build_name == null || test_build_name.equals("")) {
			test_build_name = selenium_configuration.getProperty("browserstack.build").replace("_", " ");
		}
		return test_build_name;
	}

	public void setTestBuildName(String test_build_name) {
		this.test_build_name = test_build_name;
	}

	public void startDriver() throws Exception {
		if(test_location == null || !test_location.equalsIgnoreCase("browserstack") || !test_location.equalsIgnoreCase("local")) {
			test_location = framework_configuration.getProperty("selenium.location");
		}
		if(test_location.equalsIgnoreCase("local")) {
			createLocalDriver(browserName);
		}else if(test_location.equalsIgnoreCase("browserstack")) {
			createBrowserStackDriver(browserName, browserVersion, platformName, platformVersion, screenResolution);
		}
	}
	

	public static class BrowserType {
		public static final String IE = "IE";
		public static final String FIREFOX = "Firefox";
		public static final String CHROME = "Chrome";
		public static final String EDGE = "Edge";
		public static final String SAFARI = "Safari";
		public static final String REMOTE = "Remote";
	}

	protected WebDriver driver;

	/**
	 * This method is used to open a webdriver, it's used for selenium grid as well
	 * 
	 * @author Hanoi Automation team
	 * @param None
	 * @return None
	 * @throws Exception
	 *             The method throws an exeption when browser is invalid or can't
	 *             start webdriver
	 */
	private WebDriver createLocalDriver(String browser) throws Exception {
		platformName = System.getProperty("os.name");
		MutableCapabilities options = createDriverOption(browser, platformName);
		try {
			if (browser.equalsIgnoreCase(BrowserType.FIREFOX)) {
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions op = (FirefoxOptions) options;
				driver = new FirefoxDriver(op);
			} else if (browser.equalsIgnoreCase(BrowserType.CHROME)) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions op = (ChromeOptions) options;
				driver = new ChromeDriver(op);
			} else if (browser.equalsIgnoreCase(BrowserType.IE)) {
				WebDriverManager.iedriver().setup();
				InternetExplorerOptions op = (InternetExplorerOptions) options;
				driver = new InternetExplorerDriver(op);
			} else if (browser.equalsIgnoreCase(BrowserType.SAFARI)) {
				WebDriverManager.safaridriver().setup();
				driver = new SafariDriver();
			} else if (browser.equalsIgnoreCase(BrowserType.EDGE)) {
				WebDriverManager.edgedriver().setup();
				EdgeOptions op = (EdgeOptions) options;
				driver = new EdgeDriver(op);
			} else {
				throw new Exception("The given local browser is not available: " + browser);
			}
			Log.info(String.format("Starting webdriver for: [%s] - [%s]", browser,platformName));

		} catch (Exception e) {
			Log.error(String.format("Cannot start webdriver for: [%s] - [%s] \n%s", browser,platformName,e.getMessage()));
			throw (e);
		}
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
		setBrowserSizeToMaximum();
		return driver;
	}

	private WebDriver createBrowserStackDriver(String browserName, String browserVersion, String platformName, String platformVersion,String screenResolution) throws Exception {
		String strBSRemoteUrl = selenium_configuration.getProperty("browserstack.remoteUrl");
		String strBSUserName = selenium_configuration.getProperty("browserstack.username");
		String strBSAccessKey = selenium_configuration.getProperty("browserstack.accessKey");
		
		String strBSProject = selenium_configuration.getProperty("browserstack.project");
		String strBSBuild = getTestBuildName();
		String strBSname = getTestSessionName();
		
		String strBrowserStackServer = String.format(strBSRemoteUrl, strBSUserName,strBSAccessKey);
		
		try {
			MutableCapabilities options = createDriverOption(browserName, platformName);
			options.setCapability("browserstack.timezone", "UTC");
			options.setCapability("browser", browserName);
			options.setCapability("browser_version", browserVersion);
			options.setCapability("os", platformName);
			options.setCapability("os_version", platformVersion);
			options.setCapability("resolution", screenResolution);
			
			options.setCapability("project", strBSProject);
			options.setCapability("build", strBSBuild);
			options.setCapability("name", strBSname);
			
			driver = new RemoteWebDriver(new URL(strBrowserStackServer), options);
			Log.info(String.format("Starting remote webdriver for: [%s] - [%s] - [%s] - [%s] - [%s] - [%s]",browserName,browserVersion,platformName,platformVersion,screenResolution,strBrowserStackServer));
		} catch (Exception e) {
			Log.error(String.format("Cannot start remove webdriver for: [%s] - [%s] - [%s] - [%s] - [%s] - [%s]",browserName,browserVersion,platformName,platformVersion,screenResolution,strBrowserStackServer));
			throw (e);
		}
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(3000, TimeUnit.MILLISECONDS);
		((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
		setBrowserSizeToMaximum();
		return driver;
	}

	private MutableCapabilities createDriverOption(String browser, String platform) {
		if (browser.equalsIgnoreCase(BrowserType.FIREFOX)) {
			FirefoxOptions options = new FirefoxOptions();
			options.addPreference("security.insecure_password.ui.enabled", false);
			options.addPreference("security.insecure_field_warning.contextual.enabled", false);
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			return options;
		} else if (browser.equalsIgnoreCase(BrowserType.CHROME)) {
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("safebrowsing.enabled", "true");
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("safebrowsing-disable-download-protection");
			options.addArguments("disable-geolocation");
			//AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); // https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
			options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
			options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
			//options.addArguments("--headless"); // only if you are ACTUALLY running headless
			options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
			options.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
			options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
			options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
			options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
				
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			return options;
		} else if (browser.equalsIgnoreCase(BrowserType.IE)) {
			InternetExplorerOptions options = new InternetExplorerOptions();
			options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			return options;
		} else if (browser.equalsIgnoreCase(BrowserType.SAFARI)) {
			SafariOptions options = new SafariOptions();
			return options;
		} else if (browser.equalsIgnoreCase(BrowserType.EDGE)) {
			EdgeOptions options = new EdgeOptions();
			options.setPageLoadStrategy("eager");
			return options;
		}
		return null;
	}


	/**
	 * To set the Browser size depending on Testing Scope
	 */
	public void setBrowserSizeToMaximum() {
		try {
			driver.manage().window().maximize();
		} catch (Exception e) {
		}
	}

	/**
	 * To get driver instance
	 * 
	 * @return
	 */
	public WebDriver getBaseDriver() {
		return this.driver;
	}

	/**
	 * This method is used to close a webdriver
	 * 
	 * @author Hanoi Automation team
	 * @param None
	 * @return None
	 * @throws Exception
	 *             The exception is thrown when can't close the webdriver.
	 */
	public void quit() throws Exception {

		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			HtmlReporter.fail("The webdriver is not closed!!!", e);
			throw (e);

		}
	}
	
	public void cleanBrowser() throws Exception {

		try {
			driver.manage().deleteAllCookies();
		    //driver.get("chrome://settings/clearBrowserData");
		    //driver.findElement(By.id("clearBrowsingDataConfirm")).click();
		    ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
		    ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
		    Thread.sleep(3000);
		    HtmlReporter.pass("Clean the browser!!!");
		} catch (Exception e) {
			HtmlReporter.fail("Cannot clean browser!!!", e);
			throw (e);
		}
	}

	/**
	 * Get Browser Type
	 * 
	 * @return Browser Type
	 */
	public String getBrowserInfor(String infor) {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

		String browserName = cap.getBrowserName();
		String browserVersion = (String) cap.getCapability("browserVersion");
		String osName = Platform.fromString((String) cap.getCapability("platformName")).name().toLowerCase();

		switch (infor) {
		case "browserName":
			return browserName;
		case "browserVersion":
			return browserVersion;
		case "platform":
			return osName;
		default:
			return "unknown information";
		}
	}

}