package com.an.automation.selenium;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.an.automation.report.HtmlReporter;
import com.an.automation.report.Log;
import com.an.automation.utility.FilePaths;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ExtendedWebDriver {

	private static final String DEFAULT_ELEMENT_SEPARATOR = " --- ";
	private static final int DEFAULT_WAITTIME_SECONDS = 10;
	public static final int IMPLICIT_WAIT_TIME = 10;
	private WebDriverWait wait;
	private WebDriver driver;
	
	public ExtendedWebDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	public void initExplicitWait() {
		wait = new WebDriverWait(driver, DEFAULT_WAITTIME_SECONDS);
		wait.ignoring(NoSuchElementException.class);
		//wait.ignoring(StaleElementReferenceException.class);
	}

	public WebElement highlightElement(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].style.border='2px solid red'", element);
		return element;
	}

	public List<WebElement> highlightElement(List<WebElement> elements) {
		for (WebElement e : elements) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].style.border='2px solid red'", e);
		}
		return elements;
	}

	public String getElementName(String element) {
		return element.split(DEFAULT_ELEMENT_SEPARATOR)[1];
	}

	public String getElemenLocator(String element) {
		return element.split(DEFAULT_ELEMENT_SEPARATOR)[0];
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findElement(String elementInfo) throws Exception {
		WebElement element = null;
		String elementName = getElementName(elementInfo);
		String[] extract = getElemenLocator(elementInfo).split("=", 2);
		String by = extract[0];
		String value = extract[1];
		try {
			if (by.equalsIgnoreCase("id")) {
				element = driver.findElement(By.id(value));
			} else if (by.equalsIgnoreCase("xpath")) {
				element = driver.findElement(By.xpath(value));
			} else if (by.equalsIgnoreCase("class")) {
				element = driver.findElement(By.className(value));
			} else if (by.equalsIgnoreCase("css")) {
				element = driver.findElement(By.cssSelector(value));
			} else if (by.equalsIgnoreCase("linkText")) {
				element = driver.findElement(By.linkText(value));
			} else if (by.equalsIgnoreCase("name")) {
				element = driver.findElement(By.name(value));
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				element = driver.findElement(By.partialLinkText(value));
			} else if (by.equalsIgnoreCase("tag")) {
				element = driver.findElement(By.tagName(value));
			}
			highlightElement(element);
		} catch (NoSuchElementException e) {
			HtmlReporter.fail("The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
					+ "] isn't found", e);
			throw new NoSuchElementException("The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo) + "] isn't found");
		} catch(StaleElementReferenceException e) {
			return findElement(elementInfo);
		} catch(Exception e) {
			HtmlReporter.fail("Error when find The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
			+ "]", e);
			throw (e);
		}
		return element;
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public List<WebElement> findElements(String elementInfo) throws Exception {
		List<WebElement> listElement = new ArrayList<WebElement>();
		String elementName = getElementName(elementInfo);
		String[] extract = getElemenLocator(elementInfo).split("=", 2);
		String by = extract[0];
		String value = extract[1];
		try {
			if (by.equalsIgnoreCase("id")) {
				listElement = driver.findElements(By.id(value));
			} else if (by.equalsIgnoreCase("xpath")) {
				listElement = driver.findElements(By.xpath(value));
			} else if (by.equalsIgnoreCase("class")) {
				listElement = driver.findElements(By.className(value));
			} else if (by.equalsIgnoreCase("css")) {
				listElement = driver.findElements(By.cssSelector(value));
			} else if (by.equalsIgnoreCase("linkText")) {
				listElement = driver.findElements(By.linkText(value));
			} else if (by.equalsIgnoreCase("name")) {
				listElement = driver.findElements(By.name(value));
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				listElement = driver.findElements(By.partialLinkText(value));
			} else if (by.equalsIgnoreCase("tag")) {
				listElement = driver.findElements(By.tagName(value));
			}
			highlightElement(listElement);
		} catch(NoSuchElementException e) {
			HtmlReporter.fail("The list element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
				+ "] isn't found", e);
			throw new NoSuchElementException("The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
				+ "] isn't found");
		} catch (StaleElementReferenceException e) {
			return findElements(elementInfo);
		} catch (Exception e) {
			HtmlReporter.fail("The list element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
			+ "] isn't found", e);
			throw (e);
		}
		
		return listElement;
	}
	
	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findChildElement(WebElement parentElement, String childElement) throws Exception {
		WebElement element = null;
		String elementName = getElementName(childElement);
		String[] extract = getElemenLocator(childElement).split("=", 2);
		String by = extract[0];
		String value = extract[1];
		try {
			if (by.equalsIgnoreCase("id")) {
				element = parentElement.findElement(By.id(value));
			} else if (by.equalsIgnoreCase("xpath")) {
				element = parentElement.findElement(By.xpath(value));
			} else if (by.equalsIgnoreCase("class")) {
				element = parentElement.findElement(By.className(value));
			} else if (by.equalsIgnoreCase("css")) {
				element = parentElement.findElement(By.cssSelector(value));
			} else if (by.equalsIgnoreCase("linkText")) {
				element = parentElement.findElement(By.linkText(value));
			} else if (by.equalsIgnoreCase("name")) {
				element = parentElement.findElement(By.name(value));
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				element = parentElement.findElement(By.partialLinkText(value));
			} else if (by.equalsIgnoreCase("tag")) {
				element = parentElement.findElement(By.tagName(value));
			}
			highlightElement(element);
		} catch(NoSuchElementException e) {
			HtmlReporter.fail("The child element : [" + elementName + "] located by : [" + getElemenLocator(childElement) 
				+ "] isn't found", e);
		throw new NoSuchElementException("The child element : [" + elementName + "] located by : [" + getElemenLocator(childElement) 
			+ "] isn't found");
		} catch (Exception e) {
			HtmlReporter.fail("The child element : [" + elementName + "] located by : [" + getElemenLocator(childElement)
					+ "] isn't found", e);
			throw (e);
		}
		return element;
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public List<WebElement> findChildElements(WebElement parent,String childElement) throws Exception {
		List<WebElement> listElement = null;
		String elementName = getElementName(childElement);
		String[] extract = getElemenLocator(childElement).split("=", 2);
		String by = extract[0];
		String value = extract[1];
		try {
			if (by.equalsIgnoreCase("id")) {
				listElement = parent.findElements(By.id(value));
			} else if (by.equalsIgnoreCase("xpath")) {
				listElement = parent.findElements(By.xpath(value));
			} else if (by.equalsIgnoreCase("class")) {
				listElement = parent.findElements(By.className(value));
			} else if (by.equalsIgnoreCase("css")) {
				listElement = parent.findElements(By.cssSelector(value));
			} else if (by.equalsIgnoreCase("linkText")) {
				listElement = parent.findElements(By.linkText(value));
			} else if (by.equalsIgnoreCase("name")) {
				listElement = parent.findElements(By.name(value));
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				listElement = parent.findElements(By.partialLinkText(value));
			} else if (by.equalsIgnoreCase("tag")) {
				listElement = parent.findElements(By.tagName(value));
			}
			//highlightElement(listElement);
		} catch (Exception e) {
			HtmlReporter.fail("The list element : [" + elementName + "] located by : [" + getElemenLocator(childElement)
					+ "] isn't found", e);
			throw (e);
		}
		return listElement;
	}

	public By getElementBy(String elementInfo) throws Exception {
		String elementName = getElementName(elementInfo);
		String[] extract = getElemenLocator(elementInfo).split("=", 2);
		String by = extract[0];
		String value = extract[1];
		try {
			// waitForPageLoad();
			if (by.equalsIgnoreCase("id")) {
				return By.id(value);
			} else if (by.equalsIgnoreCase("xpath")) {
				return By.xpath(value);
			} else if (by.equalsIgnoreCase("class")) {
				return By.className(value);
			} else if (by.equalsIgnoreCase("css")) {
				return By.cssSelector(value);
			} else if (by.equalsIgnoreCase("linkText")) {
				return By.linkText(value);
			} else if (by.equalsIgnoreCase("name")) {
				return By.name(value);
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				return By.partialLinkText(value);
			} else if (by.equalsIgnoreCase("tag")) {
				return By.tagName(value);
			}
		} catch (Exception e) {
			HtmlReporter.fail("The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
					+ "] isn't found", e);
			throw (e);
		}
		return null;
	}

	/**
	 * Set the time out to wait for page load
	 * 
	 * @param seconds
	 *            Wait time in seconds
	 */
	public void setImplicitWaitTime(int seconds) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(seconds,
			// TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(seconds, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	
	/**
	 * Execute javascript. This method used to execute a javascript
	 * 
	 * @author Hanoi Automation team
	 * @param jsFunction
	 *            the js function
	 * @throws Exception
	 *             The exception is thrown if can't execute java script
	 */
	public void executeJavascript(String jsFunction) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript(jsFunction);
			HtmlReporter.pass("Excecuted the java script: [" + jsFunction + "]");
		} catch (Exception e) {
			HtmlReporter.fail("Can't excecute the java script: [" + jsFunction + "]", e);
			throw (e);
		}
	}

	/**
	 * This method is used to execute a java script function for an object argument.
	 * 
	 * @author Hanoi Automation team
	 * @param jsFunction
	 *            The java script function
	 * @param object
	 *            The argument to execute script
	 * @throws Exception
	 *             The exception is thrown if object is invalid.
	 */
	public void executeJavascript(String jsFunction, Object... object) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript(jsFunction, object);
			HtmlReporter.pass("Excecute the java script: [" + jsFunction + "] for the object: [" + object + "]");
		} catch (Exception e) {
			HtmlReporter.fail("Can't excecute the java script: [" + jsFunction + "] for the object: [" + object + "]",
					e);
			throw (e);

		}
	}

	/**
	 * This method is used to wait for the page load
	 * 
	 * @author Hanoi Automation team
	 * @param
	 * @return None
	 * @throws Exception
	 */
	public void waitForPageLoad() {

		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAITTIME_SECONDS);

		// Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		// JQuery Wait
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0;
			}
		};

		// Angular Wait
		String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length";
		ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver).executeScript(angularReadyScript) == 0;
			}
		};

		wait.until(jsLoad);
		// wait.until(jQueryLoad);
		// wait.until(angularLoad);
	}
	
	/**
	 * This method is used to navigate the browser to the url
	 * 
	 * @author Hanoi Automation team
	 * @param url
	 *            the url of website
	 * @return None
	 * @throws Exception
	 *             The exception is thrown if the driver can't navigate to the url
	 */
	public void openUrl(String url) throws Exception {
		try {
			driver.get(url);
			HtmlReporter.pass("Navigated to the url : [" + url + "]");
		} catch (Exception e) {
			HtmlReporter.fail("Can't navigate to the url : [" + url + "]", e);
			throw (e);

		}
	}
	
	public String getCurrentURL() {
		String currentURL = driver.getCurrentUrl();
		HtmlReporter.pass("Currrent URL: [" + currentURL + "]");
		return currentURL;
	}
	
	public void clearText(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			findElement(element).clear();
		} catch (StaleElementReferenceException e) {
			clearText(element);
		}catch (Exception e) {
			HtmlReporter.fail("Can't clear text of the element: [" + elementName + "]", e);
			throw (e);
		} 
	}

	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if sending keys not success
	 */
	public void sendkeys(String element, CharSequence keysToSend) throws Exception {
		String elementName = getElementName(element);
		try {
			WebElement e = findElement(element);
			e.sendKeys(keysToSend);
			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + elementName + "]");
		} catch (StaleElementReferenceException e) {
			sendkeys(element,keysToSend);
		}catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + elementName + "]", e);
			throw (e);
		} 
	}
	
	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if sending keys not success
	 */
	public void sendKeys(String element, CharSequence keysToSend,int delay) throws Exception {
		String elementName = getElementName(element);
		try {
			WebElement e = findElement(element);
			for (int i = 0; i < keysToSend.length(); i++){
			    char c = keysToSend.charAt(i);        
			    e.sendKeys(Character.toString(c));
				Thread.sleep(delay);
			}
			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + elementName + "]");
		} catch (StaleElementReferenceException e) {
			sendKeys(element,keysToSend,delay);
		}catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + elementName + "]", e);
			throw (e);
		} 
	}
	
	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if sending keys not success
	 */
	public void sendkeysByAction(String element, CharSequence keysToSend) throws Exception {
		String elementName = getElementName(element);
		try {
			WebElement e = findElement(element);
			String platform = System.getProperty("os.name");
			if(platform.toLowerCase().contains("mac")) {
				new Actions(driver).click(e)
		        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
		        .pause(200).sendKeys(Keys.BACK_SPACE)
		        .pause(200).sendKeys(keysToSend).perform();
			}else {
				new Actions(driver).click(e)
		        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
		        .pause(200).sendKeys(Keys.BACK_SPACE)
		        .pause(200).sendKeys(keysToSend).perform();
			}
			
			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + elementName + "]");
		} catch (StaleElementReferenceException e) {
			sendkeys(element,keysToSend);
		}catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + elementName + "]", e);
			throw (e);
		} 
	}
	
	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if sending keys not success
	 */
	public void sendkeysByAction(WebElement parent,String element, String keysToSend) throws Exception {
		String elementName = getElementName(element);
		try {
			WebElement ele = findChildElement(parent,element);
			String platform = System.getProperty("os.name");
			if(platform.toLowerCase().contains("mac")) {
				new Actions(driver).click(ele)
		        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
		        .pause(200).sendKeys(Keys.BACK_SPACE)
		        .pause(200).sendKeys(keysToSend).perform();
			}else {
				new Actions(driver).click(ele)
		        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
		        .pause(200).sendKeys(Keys.BACK_SPACE)
		        .pause(200).sendKeys(keysToSend).perform();
			}
			
			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + elementName + "]");
		} catch (StaleElementReferenceException e) {
			sendkeys(element,keysToSend);
		}catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + elementName + "]", e);
			throw (e);
		} 
	}

	/**
	 * Get the text of a web element
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of web element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getText(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			String text = "";
			WebElement e = findElement(element);
			if(e.getTagName().equalsIgnoreCase("input")) {
				text = e.getAttribute("value");
			}else{
				text = e.getText();
			}
			HtmlReporter.pass("Element [" + elementName + "] has text: [" + text + "]");
			return text;
		} catch(StaleElementReferenceException e) {
			return getText(element);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't get text of element: [" + elementName + "]", e);
			throw e;
		}
	}
	
	/**
	 * Get the text of a web element
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of web element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getText(WebElement parent,String childElement) throws Exception {
		String elementName = getElementName(childElement);
		try {
			String text = "";
			WebElement e = findChildElement(parent,childElement);
			highlightElement(e);
			if(e.getTagName().equalsIgnoreCase("input")) {
				text = e.getAttribute("value");
			}else if(e.getTagName().equalsIgnoreCase("select")) {
				text = new Select(e).getFirstSelectedOption().getText();
			}else{
				text = e.getText();
			}
			HtmlReporter.pass("Element [" + elementName + "] has text: [" + text + "]");
			return text;
		} catch(StaleElementReferenceException e) {
			return getText(childElement);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't get text of element: [" + elementName + "]", e);
			throw e;
		}
	}
	
	
	/**
	 * Get the text of a list web elements
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of web element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public List<String> getTexts(String element) throws Exception {
		String elementName = getElementName(element);
		List<String> texts = new ArrayList<String>();
		try {
			String output = "";
			List<WebElement> elements = findElements(element);
			for(WebElement e : elements) {
				if(e.getTagName().equalsIgnoreCase("input")) {
					texts.add(e.getAttribute("value"));
					output += "[" + e.getAttribute("value") + "]-";
				}else{
					texts.add(e.getText());
					output += "-[" + e.getText() + "]";
				}
			}
			
			HtmlReporter.pass("List Elements [" + elementName + "] has text: " + output + "");
			return texts;
		} catch(StaleElementReferenceException e) {
			return getTexts(element);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't get text of list elements: [" + elementName + "]", e);
			throw e;
		}
	}
	
	public String getTitle() throws Exception {
		try {
			waitForPageLoad();
			String title = driver.getTitle();
			HtmlReporter.pass("Current title is: [" + title + "]");
			return title;
		} catch (Exception e) {
			HtmlReporter.fail("Cannot get title of screen", e);
			throw e;
		}
	}

	/**
	 * Get the text of a selected option from a Dropdown list
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of selected element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getTextSelectedDDL(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			String text = "";
			Select ddl = new Select(findElement(element));
			text = ddl.getFirstSelectedOption().getText();
			HtmlReporter.pass("Current value of [" + elementName + "] is : [" + text + "]");
			return text;
		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of Dropdown: [" + elementName + "]", e);
			return "";

		}
	}

	/**
	 * Get the text of a Dropdown list
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of a dropdown list
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getTextDDL(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			String text = "";
			Select ddl = new Select(findElement(element));
			for (WebElement option : ddl.getOptions()) {
				text = text + option.getText();
			}
			HtmlReporter.pass("Got the text of Dropdown [" + elementName + "] is : [" + text + "]");
			return text;

		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of Dropdown: [" + elementName + "]", e);
			return "";

		}
	}

	/**
	 * Get the attribute value of a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param attribute
	 *            The attribute need to get value
	 * @return The attribute value as string
	 * @throws Exception
	 */
	public String getAttribute(String element, String attribute) throws Exception {
		String elementName = getElementName(element);
		try {
			String attributeValue = findElement(element).getAttribute(attribute);
			HtmlReporter.pass("Attribute [" + attribute + "] of element [" + elementName + "] is [" + attributeValue + "]");
			return attributeValue;
		} catch(StaleElementReferenceException e) {
			return getAttribute(element,attribute);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't get the attribute [" + attribute + "] of element [" + elementName + "]", e);
			throw e;

		}
	}
	
	/**
	 * Get the attribute value of a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param attribute
	 *            The attribute need to get value
	 * @return The attribute value as string
	 * @throws Exception
	 */
	public String getAttribute(WebElement parent, String childElement,String attribute) throws Exception {
		String elementName = getElementName(childElement);
		try {
			String attributeValue = findChildElement(parent, childElement).getAttribute(attribute);
			HtmlReporter.pass("Attribute [" + attribute + "] of element [" + elementName + "] is [" + attributeValue + "]");
			return attributeValue;
		} catch(StaleElementReferenceException e) {
			return getAttribute(parent,childElement,attribute);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't get the attribute [" + attribute + "] of element [" + elementName + "]", e);
			throw e;

		}
	}
	
	/**
	 * set the attribute value of a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param attribute
	 *            The attribute need to set value
	 * @param value
	 *            value to set
	 * @throws Exception
	 */
	public void setAttribute(String element, String attribute,String value) throws Exception {
		String elementName = getElementName(element);
		try {
			WebElement e = findElement(element);
			executeJavascript("arguments[0].setAttribute(arguments[1], arguments[2]);", 
	                e, attribute, attribute);
			HtmlReporter.pass("Set Attribute [" + attribute + "] of element [" + elementName + "] to [" + value + "]");
		} catch(StaleElementReferenceException e) {
			setAttribute(element,attribute,attribute);
		}catch (Exception e) {
			HtmlReporter.fail("Can't set the attribute [" + attribute + "] to element [" + elementName + "]", e);
			throw e;

		}
	}

	/**
	 * Click on a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void click(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			findElement(element).click();
			HtmlReporter.pass("Click on the element: [" + elementName + "]");
		} catch(StaleElementReferenceException e) {
			click(element);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't click on the element: [" + elementName + "]", e);
			throw (e);
		}
	}
	
	/**
	 * Click on a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void click(WebElement parent, String element) throws Exception {
		String elementName = getElementName(element);
		try {
			findChildElement(parent, element).click();
			HtmlReporter.pass("Click on the element: [" + elementName + "]");
		} catch(StaleElementReferenceException e) {
			click(element);
		}
		catch (Exception e) {
			HtmlReporter.fail("Can't click on the element: [" + elementName + "]", e);
			throw (e);
		}
	}

	/**
	 * Perform double click
	 * 
	 * @param by
	 *            The By locator object of element
	 * @param elementName
	 *            Name of element used to write
	 * @return
	 * @throws Exception
	 */

	public void doubleClick(String element) throws Exception {
		String elementName = getElementName(element);
		try {

			Actions action = new Actions(driver);
			action.moveToElement(findElement(element)).doubleClick().build().perform();
			HtmlReporter.pass("DoubleClick on the element: [" + elementName + "]");
		} catch(StaleElementReferenceException e) {
			doubleClick(element);
		} catch (Exception e) {
			HtmlReporter.fail("DoubleClick on the element: [" + elementName + "] failed", e);
			throw e;

		}
	}

	/**
	 * Click on a web element using javascript
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void clickByJS(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			executeJavascript("arguments[0].click();", findElement(element));
			HtmlReporter.pass("Click by JavaScript on the element: [" + elementName + "]");
		} catch(StaleElementReferenceException e) {
			clickByJS(element);
		} catch (Exception e) {
			HtmlReporter.fail("Can't click by Java Script on the element: [" + elementName + "]", e);
			throw (e);

		}
	}

	/**
	 * Move to the element then click
	 * 
	 * @param elementName
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void clickByAction(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			Actions action = new Actions(driver);
			WebElement elem = findElement(element);
			action.click(elem).build().perform();
			HtmlReporter.pass("Click by Actions on the element: [" + elementName + "]");
		} catch(StaleElementReferenceException e) {
			clickByJS(element);
		} catch (Exception e) {
			HtmlReporter.fail("Click by Actions on [" + elementName + "] failed", e);
			throw e;

		}
	}

	/**
	 * Select a radio button
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void selectRadioButton(String element) throws Exception {
		String elementName = getElementName(element);
		try {

			WebElement rbElement = findElement(element);
			if (!rbElement.isSelected()) {
				rbElement.click();
			}
			HtmlReporter.pass("Radio button element: [" + elementName + "] is selected.");

		} catch(StaleElementReferenceException e) {
			selectRadioButton(element);
		} catch (Exception e) {
			HtmlReporter.fail("Radio button element: [" + elementName + "] isn't selected.", e);
			throw (e);
		}

	}

	/**
	 * Select a check box
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void selectCheckBox(String element) throws Exception {
		String elementName = getElementName(element);
		try {

			WebElement chkElement = findElement(element);

			if (!chkElement.isSelected()) {
				chkElement.click();
			}
			HtmlReporter.pass("Checkbox element: [" + elementName + "] is selected.");

		} catch (StaleElementReferenceException e) {
			selectRadioButton(element);
		} catch (Exception e) {
			HtmlReporter.fail("Checkbox element: [" + elementName + "] isn't selected.", e);
			throw (e);
		}

	}

	/**
	 * De-select a check box
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void deselectCheckBox(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			WebElement chkElement = findElement(element);

			if (chkElement.isSelected()) {
				chkElement.click();
			}
			HtmlReporter.pass("Checkbox element: [" + elementName + "] is deselected.");

		} catch (StaleElementReferenceException e) {
			deselectCheckBox(element);
		} catch (Exception e) {
			HtmlReporter.fail("Checkbox element: [" + elementName + "] isn't deselected.", e);
			throw (e);
		}

	}

	/**
	 * Select an option in the Drop Down list
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param chosenOption
	 *            The option is chosen
	 * @throws Exception
	 */
	public void selectDDLByText(String element, String chosenOption) throws Exception {
		String elementName = getElementName(element);
		try {
			Select ddl = new Select(findElement(element));
			ddl.selectByVisibleText(chosenOption);
			HtmlReporter.pass("Select option by Text: [" + chosenOption + "] from select box: [" + elementName + "]");

		} catch (StaleElementReferenceException e) {
			selectDDLByText(element,chosenOption);
		} catch (Exception e) {
			HtmlReporter.fail(
					"Can't select option: [" + chosenOption + "] by Text from the select box: [" + elementName + "]",
					e);

			throw (e);
		}
	}

	/**
	 * Select an option in the Drop Down list by value
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param value
	 *            The value is chosen
	 * @throws Exception
	 */
	public void selectDDLByValue(String element, String value) throws Exception {
		String elementName = getElementName(element);
		try {

			Select ddl = new Select(findElement(element));
			ddl.selectByValue(value);
			HtmlReporter.pass("Select option by Value: [" + value + "] from select box: [" + elementName + "]");

		} catch (StaleElementReferenceException e) {
			selectDDLByValue(element,value);
		} catch (Exception e) {
			HtmlReporter.fail(
					"Can't select option: [" + value + "] by Value from the select box: [" + elementName + "]", e);

			throw e;
		}
	}
	
	public boolean isTitleToBe(String expectedTitle, int timeout) throws Exception {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			boolean result = wait.until(ExpectedConditions.titleIs(expectedTitle));
			if (result) {
				HtmlReporter.info(String.format("Title is become matched with expectation: [%s]",expectedTitle));
			} else {
				System.out.println("currently1: " + driver.getTitle());
				HtmlReporter.warning(String.format("Title is not become matched with expectation: [%s]",expectedTitle));
			}
			return result;
		} catch (TimeoutException e) {
			System.out.println("currently2: " + driver.getTitle());
			HtmlReporter.warning(String.format("Title is not become matched with expectation: [%s]",expectedTitle));
			return false;
		}
	}
	
	public boolean isTextOfElementToBe(String elementInfo, String expectedText, int timeout) throws Exception {
		String elementName = getElementName(elementInfo);
		By eBy = getElementBy(elementInfo);
		try {
			setImplicitWaitTime(timeout);
			wait.withTimeout(Duration.ofSeconds(timeout));
			// wait.until((driver) -> !element.getText().equals(""));
			wait.until(new ExpectedCondition<String>() {
				@Override
				public String apply(WebDriver driver) {
					String currenText;
					try {
						currenText = driver.findElement(eBy).getText().replace("\\n", "").replace("\n", "");
					} catch (StaleElementReferenceException e) {
						return null;
					}catch (NoSuchElementException e) {
						return null;
					}
					return currenText.equals(expectedText) ? currenText : null;
				}

				@Override
				public String toString() {
					return String.format("Current text: [%s]", driver.findElement(eBy).getText());
				}
			});
			return true;
		} catch (TimeoutException e) {
			Log.warn(String.format("Text of Element [%s] is not become [%s] in expected time = %s", elementName,expectedText,timeout));
			throw e;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Text of Element [%s] is not become [%s] in expected time = %s", elementName,expectedText,timeout),e);
			throw e;
		}finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
	}
	
	public boolean isTextOfElementToContains(String elementInfo, String expectedText, int timeout) throws Exception {
		String elementName = getElementName(elementInfo);
		By eBy = getElementBy(elementInfo);
		try {
			setImplicitWaitTime(timeout);
			wait.withTimeout(Duration.ofSeconds(timeout));
			// wait.until((driver) -> !element.getText().equals(""));
			wait.until(new ExpectedCondition<String>() {
				@Override
				public String apply(WebDriver driver) {
					String currenText;
					try {
						currenText = driver.findElement(eBy).getText();
					} catch (StaleElementReferenceException e) {
						return null;
					}catch (NoSuchElementException e) {
						return null;
					}
					return currenText.contains(expectedText) ? currenText : null;
				}

				@Override
				public String toString() {
					return String.format("Current text: [%s]", driver.findElement(eBy).getText());
				}
			});
			return true;
		} catch (TimeoutException e) {
			Log.warn(String.format("Text of Element [%s] is not become contains [%s] in expected time = %s", elementName,expectedText,timeout));
			throw e;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Text of Element [%s] is not become contains [%s] in expected time = %s", elementName,expectedText,timeout),e);
			throw e;
		}finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
	}
	
	public boolean isElementEnabled(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			boolean check = findElement(element).isEnabled();
			if(check) {
				Log.info(String.format("Element: [%s] is enabled", elementName));
				return true;
			}else{
				Log.info(String.format("Element: [%s] is disabled", elementName));
				return false;
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	/**
	 * check if element displayed after expected time
	 * 
	 * @param element
	 *            xpath=xxx --- elementname
	 * @param time
	 *            Time to wait in seconds
	 * @return false if element not displayed
	 * @throws Exception 
	 */
	public boolean isElementDisplayed(String element) throws Exception {
		return isElementDisplayed(element,IMPLICIT_WAIT_TIME);
	}


	/**
	 * check if element displayed after expected time
	 * 
	 * @param element
	 *            xpath=xxx --- elementname
	 * @param time
	 *            Time to wait in seconds
	 * @return false if element not displayed
	 * @throws Exception 
	 */
	public boolean isElementDisplayed(String element, int timeout) throws Exception {
		String elementName = getElementName(element);
		try {
			setImplicitWaitTime(timeout);
			By eby = getElementBy(element);
			//WebElement e = findElement(element);
			//boolean result = e.isDisplayed();
			wait.withTimeout(Duration.ofSeconds(timeout));
			WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(eby));
			if (e != null) {
				highlightElement(e);
				Log.info(String.format("Element: [%s] is displayed", elementName));
				return true;
			} else {
				Log.warn(String.format("Element: [%s] is not displayed", elementName));
				return false;
			}
		} catch (NoSuchElementException e) {
			Log.warn(String.format("Element: [%s] is not displayed in %s", elementName, timeout));
			return false;
		} catch(StaleElementReferenceException e) {
			return isElementDisplayed(element,timeout);
		} catch(TimeoutException e){
			Log.warn(String.format("Element: [%s] is not displayed in %s", elementName, timeout));
			return false;
		}finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
	}
	
	/**
	 * check if element displayed after expected time
	 * 
	 * @param element
	 *            xpath=xxx --- elementname
	 * @param time
	 *            Time to wait in seconds
	 * @throw throw exception if element is not displayed
	 * @throws Exception 
	 */
	public boolean waitForElementDisplayed(String element, int timeout) throws Exception {
		String elementName = getElementName(element);
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			By eBy = getElementBy(element);
			wait.until(ExpectedConditions.visibilityOfElementLocated(eBy));
			HtmlReporter
					.pass(String.format("Element: [%s] is displayed in expected time = %s", elementName, timeout));
		} catch (TimeoutException e) {
			HtmlReporter.fail(String.format("Element [%s] is not displayed in expected time = %s", elementName, timeout),e);
			throw e;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
		return true;
	}

	/**
	 * check if element disappear after expected time
	 * 
	 * @param element
	 *            xpath=xxx --- elementname
	 * @param time
	 *            Time to wait in seconds
	 * @return false if element not displayed
	 * @throws Exception 
	 */
	public boolean isElementDisappear(String element, int timeout) throws Exception {
		String elementName = getElementName(element);
		try {
			setImplicitWaitTime(0);
			By eBy = getElementBy(element);
			wait.withTimeout(Duration.ofSeconds(timeout));
			boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(eBy));
			if (result) {
				Log.info(String.format("Element: [%s] is disappeared", elementName));
			} else {
				Log.warn(String.format("Element: [%s] is not disappeared", element.toString()));
			}
			return result;
		} catch (TimeoutException e) {
			Log.warn(String.format("Element: [%s] is not disappeared in %s", element.toString(), timeout));
			return false;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
	}
	
	/**
	 * wait for element disappeared after expected time
	 * 
	 * @param element
	 *            xpath=xxx --- elementname
	 * @param time
	 *            Time to wait in seconds
	 * @throw throw exception if element is not disappear
	 * @throws Exception 
	 */
	public boolean waitForElementDisappear(String element, int timeout) throws Exception {
		String elementName = getElementName(element);
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			By eBy = getElementBy(element);
			boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(eBy));
			if(result) {
				HtmlReporter.pass(String.format("Element: [%s] is disppeared in expected time = %s", elementName, timeout));
			}else {
				HtmlReporter.pass(String.format("Element: [%s] is not disppeared in expected time = %s", elementName, timeout));
			}
		} catch (TimeoutException e) {
			HtmlReporter.fail(String.format("Element [%s] is not displayed in expected time = %s", elementName, timeout),e);
			throw e;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
		return true;
	}

	/**
	 * Wait for a time until presenceOfElementLocated
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public WebElement waitForElementPresent(String element, int timeout) throws Exception {
		String elementName = getElementName(element);
		try {
			By eBy = getElementBy(element);
			wait.withTimeout(Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.presenceOfElementLocated(eBy));
		} catch (Exception e) {
			HtmlReporter.fail("The element : [" + elementName + "] located by : [" + getElemenLocator(element)
					+ "] isn't present", e);
			throw e;
		}
	}

	/**
	 * Wait for a time until elementToBeClickable
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public WebElement waitForElementToBeClickable(String element, int timeout) throws Exception {
		String elementName = getElementName(element);
		try {
			By eBy = getElementBy(element);
			wait.withTimeout(Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.elementToBeClickable(eBy));
		} catch (Exception e) {
			HtmlReporter.fail("The element : [" + elementName + "] located by : [" + getElemenLocator(element)
					+ "] isn't able to click", e);
			throw e;
		}
	}
	
	public String waitForTextOfElement(String elementInfo, String expectedText, int timeout) throws Exception {
		String text = "";
		String elementName = getElementName(elementInfo);
		By eBy = getElementBy(elementInfo);
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			// wait.until((driver) -> !element.getText().equals(""));
			text = wait.until(new ExpectedCondition<String>() {
				@Override
				public String apply(WebDriver driver) {
					String currenText;
					try {
						currenText = driver.findElement(eBy).getText();
					} catch (StaleElementReferenceException e) {
						return null;
					}catch (NoSuchElementException e) {
						return null;
					}
					return currenText.equals(expectedText) ? currenText : null;
				}

				@Override
				public String toString() {
					return String.format("Current text: [%s]", driver.findElement(eBy).getText());
				}
			});
			return text;
		} catch (TimeoutException e) {
			HtmlReporter.fail(String.format("Text of Element [%s] is not become [%s] in expected time = %s", elementName,expectedText,timeout),e);
			throw e;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Text of Element [%s] is not become [%s] in expected time = %s", elementName,expectedText,timeout),e);
			throw e;
		}finally {
			setImplicitWaitTime(IMPLICIT_WAIT_TIME);
		}
	}

	/**
	 * Checking a web element is present or not
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return True if the element is present, False if the element is not present
	 * @throws Exception
	 */
	public boolean isElementPresent(String element) {
		try {
			findElement(element);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * Upload file using Robot
	 * 
	 * @param filePath
	 *            Url to file upload
	 * @return The WebElement object
	 * @throws Exception
	 */
	/////// this function only work in local
	// public void uploadfile(String filePath) throws Exception {
	//
	// try {
	// filePath = FilePaths.correctPath(filePath);
	// String strBrowserType = getBrowserInfor("browserName");
	//
	// StringSelection selection = new StringSelection(filePath);
	// Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	// clipboard.setContents(selection, selection);
	// Robot robot = new Robot();
	// if (strBrowserType.equalsIgnoreCase(BrowserType.SAFARI)) {
	// // Cmd + Tab is needed since it launches a Java app and the
	// // browser looses focus
	// robot.keyPress(KeyEvent.VK_META);
	// robot.keyPress(KeyEvent.VK_TAB);
	//
	// robot.keyRelease(KeyEvent.VK_META);
	// robot.keyRelease(KeyEvent.VK_TAB);
	// robot.delay(500);
	//
	// // Open Goto window
	// robot.keyPress(KeyEvent.VK_META);
	// robot.keyPress(KeyEvent.VK_SHIFT);
	// robot.keyPress(KeyEvent.VK_G);
	//
	// robot.keyRelease(KeyEvent.VK_META);
	// robot.keyRelease(KeyEvent.VK_SHIFT);
	// robot.keyRelease(KeyEvent.VK_G);
	// robot.delay(500);
	//
	// // Paste the clipboard value
	// robot.keyPress(KeyEvent.VK_META);
	// robot.keyPress(KeyEvent.VK_V);
	//
	// robot.keyRelease(KeyEvent.VK_META);
	// robot.keyRelease(KeyEvent.VK_V);
	// robot.delay(500);
	//
	// // Press Enter key to close the Goto window and Upload window
	// robot.keyPress(KeyEvent.VK_ENTER);
	// robot.keyRelease(KeyEvent.VK_ENTER);
	// robot.delay(500);
	//
	// robot.keyPress(KeyEvent.VK_ENTER);
	// robot.keyRelease(KeyEvent.VK_ENTER);
	//
	// } else if (strBrowserType.equalsIgnoreCase(BrowserType.EDGE)) {
	//
	// robot.keyPress(KeyEvent.VK_CONTROL);
	// robot.keyPress(KeyEvent.VK_V);
	// robot.keyRelease(KeyEvent.VK_V);
	// robot.keyRelease(KeyEvent.VK_CONTROL);
	// robot.delay(1000);
	// robot.keyPress(KeyEvent.VK_ENTER);
	// robot.keyRelease(KeyEvent.VK_ENTER);
	//
	// } else {
	//
	// robot.keyPress(KeyEvent.VK_CONTROL);
	// robot.keyPress(KeyEvent.VK_V);
	// robot.keyRelease(KeyEvent.VK_V);
	// robot.keyRelease(KeyEvent.VK_CONTROL);
	// robot.delay(1000);
	// robot.keyPress(KeyEvent.VK_ENTER);
	// robot.keyRelease(KeyEvent.VK_ENTER);
	// }
	//
	// HtmlReporter.pass("Upload file [" + filePath + "]");
	//
	// } catch (Exception e) {
	// Log.error("uploaded fail ");
	// HtmlReporter.fail("Uploaded fail ", e);
	//
	// throw (e);
	// }
	// }

	/**
	 * Open url in new tab
	 * 
	 * @param url
	 *            Url to of new tab *
	 * @throws Exception
	 */
	public void openNewTab(String url) throws Exception {
		try {
			// Open tab 2 using CTRL + t keys.
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
			// Open URL In 2nd tab.
			driver.get(url);
			// Switch to current selected tab's content.
			driver.switchTo().defaultContent();
			HtmlReporter.pass("Opened new tab with url [" + url + "]");
		} catch (Exception e) {
			HtmlReporter.pass("Failed to open new tab with url [" + url + "]");
			throw (e);

		}
	}

	/**
	 * Verify the present of an alert
	 * 
	 * @return
	 */
	public boolean isAlertPresent(int timeout) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.alertIsPresent());
			HtmlReporter.pass("Alert is presented in expected time");
			return true;
		} catch (Exception Ex) {
			HtmlReporter.pass("Alert is presented in expected time");
			return false;
		}
	}

	/**
	 * Accept an Alert
	 * 
	 * @throws Exception
	 */
	public void acceptAlert() throws Exception {
		try {
			if (isAlertPresent(1)) {
				driver.switchTo().alert().accept();
				HtmlReporter.pass("Accept Alert");
			}
		} catch (Exception e) {
			HtmlReporter.fail("Can't accept Alert", e);
			throw (e);
		}
	}


	/**
	 * Hide an element by javascript
	 * 
	 * @param by
	 *            By locator
	 * @throws Exception
	 */
	public void hideElement(By by) throws Exception {
		try {
			WebElement element = driver.findElement(by);
			executeJavascript("arguments[0].style.visibility='hidden'", element);
			waitForPageLoad();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Perform mouse hover action
	 * 
	 * @param by
	 *            The By locator object of element
	 * @param elementName
	 *            Name of element used to write
	 * @return
	 * @throws Exception
	 */

	public void mouseHover(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			Actions action = new Actions(driver);
			action.moveToElement(findElement(element)).perform();
			HtmlReporter.pass("mouseHover [" + elementName + "] successfully");
		} catch (Exception e) {
			HtmlReporter.fail("mouseHover [" + elementName + "] failed", e);
			throw e;
		}
	}

	/**
	 * Scroll the web page to the element
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void scrollIntoView(String element) throws Exception {
		String elementName = getElementName(element);
		try {
			executeJavascript("arguments[0].scrollIntoView(true);", findElement(element));
			wait(2);
			HtmlReporter.pass("Scroll into [" + elementName + "] successfully");
		} catch (Exception e) {
			HtmlReporter.fail("Can not scroll into [" + elementName + "]", e);
			throw (e);

		}
	}

	/**
	 * This method is used to capture a screenshot then write to the TestNG Logger
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot() {

		String failureImageFileName = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss.SSS")
				.format(new GregorianCalendar().getTime()) + ".jpg";
		try {

			if (driver != null) {
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String screenShotDirector = FilePaths.getScreenshotFolder();
				FileUtils.copyFile(scrFile, new File(screenShotDirector + File.separator + failureImageFileName));
				return screenShotDirector + File.separator + failureImageFileName;
			}
			return "";
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * This method is used to capture a screenshot
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot(String filename) throws Exception {

		String screenShotDirector = FilePaths.getScreenshotFolder();
		String screenshotFile = FilePaths.correctPath(screenShotDirector + filename);

		try {
			if (driver != null) {
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(screenshotFile));
				return screenshotFile;

			} else {
				return "";
			}
		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * This method is used to capture a screenshot with Ashot
	 * 
	 * @author Hanoi Automation team
	 * @param filename
	 * @return The screenshot path
	 * @throws Exception
	 */
	public String takeScreenshotWithAshot(String fileDir) throws Exception {
		fileDir = FilePaths.correctPath(fileDir);
		try {

			if (driver != null) {
				Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
						.takeScreenshot(driver);
				ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
			} else {
				fileDir = "";
			}

		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			throw e;
		}
		return fileDir;
	}

	/**
	 * This method is used to capture an element's screenshot with Ashot
	 * 
	 * @author Hanoi Automation team
	 * @param filename
	 * @return The screenshot path
	 * @throws Exception
	 */
	public String takeScreenshotWithAshot(String fileDir, String element) throws Exception {
		fileDir = FilePaths.correctPath(fileDir);
		try {

			if (driver != null) {
				WebElement e = findElement(element);
				Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
						.takeScreenshot(driver, e);
				ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
			}

		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			throw e;
		}
		return fileDir;

	}

	/**
	 * To compare the layout of a web page with baseline
	 * 
	 * @param filename
	 *            The name of screenshot
	 * @throws Exception
	 */
	/*
	 * public void compareScreenshot(String filename) throws Exception { String
	 * screenshotFileName = filename + "." +
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"); String baseLineImage =
	 * HtmlReporter.strBaseLineScreenshotFolder + screenshotFileName; String
	 * actualImage = HtmlReporter.strActualScreenshotFolder + screenshotFileName; //
	 * String diffImage = Common.strWebDiffScreenshotFolder + screenshotFileName;
	 * try { waitForPageLoad(); if (!Common.pathExist(baseLineImage)) {
	 * takeScreenshotWithAshot(baseLineImage); } else {
	 * takeScreenshotWithAshot(actualImage); ImageCompare imageComparitor = new
	 * ImageCompare(); BufferedImage diffBuff =
	 * imageComparitor.diffImages(baseLineImage, actualImage, 30, 10); if (diffBuff
	 * == null) { Log.info("The actual screenshot of page [" + filename +
	 * "] matches with the baseline"); } else {
	 * Log.error("The actual screenshot of page [" + filename +
	 * "] doesn't match with the baseline"); ImageIO.write(diffBuff,
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"), new
	 * File(HtmlReporter.strDiffScreenshotFolder, screenshotFileName)); throw new
	 * Exception("The actual screenshot doesn't match with the baseline"); } } }
	 * catch (Exception e) { throw e; } }
	 */

	/**
	 * To compare the layout of a web element with baseline
	 * 
	 * @param filename
	 *            The name of screenshot
	 * @throws Exception
	 */
	/*
	 * public void compareScreenshot(String filename, String element) throws
	 * Exception { String screenshotFileName = filename + "." +
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"); String baseLineImage =
	 * HtmlReporter.strBaseLineScreenshotFolder + screenshotFileName; String
	 * actualImage = HtmlReporter.strActualScreenshotFolder + screenshotFileName; //
	 * String diffImage = Common.strWebDiffScreenshotFolder + screenshotFileName;
	 * try { waitForPageLoad(); if (!Common.pathExist(baseLineImage)) {
	 * takeScreenshotWithAshot(baseLineImage, element); } else {
	 * takeScreenshotWithAshot(actualImage, element); ImageCompare imageComparitor =
	 * new ImageCompare(); BufferedImage diffBuff =
	 * imageComparitor.diffImages(baseLineImage, actualImage, 30, 10); if (diffBuff
	 * == null) { Log.info("The actual screenshot of element [" + filename +
	 * "] matches with the baseline"); } else {
	 * Log.error("The actual screenshot of element [" + filename +
	 * "] doesn't match with the baseline"); ImageIO.write(diffBuff,
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"), new
	 * File(HtmlReporter.strDiffScreenshotFolder, screenshotFileName)); throw new
	 * Exception( "The actual screenshot of element [" + filename +
	 * "] doesn't match with the baseline"); } } } catch (Exception e) { throw e; }
	 * }
	 */

	public void switchWindowByTitle(String title) throws Exception {
		try {
			boolean switched = false;
			Set<String> allWindows = driver.getWindowHandles();
			for (String windowHandle : allWindows) {
				driver.switchTo().window(windowHandle);
				if (driver.getTitle().equals(title)) {
					HtmlReporter.pass("Switched to Window with title:" + title);
					switched = true;
					break;
				}
			}
			if (!switched) {
				HtmlReporter.fail("Cannot find any window with title: " + title + " to switch");
				}
		} catch (Exception e) {
			HtmlReporter.fail("Switched to Window with title:" + title);
		}
	}
	
	
	
	//only use for browserstack
	public void markTestStatus(String status, String reason) throws Exception {
		//need check for browserstack here
		if(false) {
			reason = reason.replace("\"", "'");
			String script = "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+status+"\", \"reason\": \""+reason+"\"}}";
			executeJavascript(script);
		}
	}

	public void wait(int second) throws InterruptedException {
		Thread.sleep(second * 1000);
	}

}
