package com.nashtech.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.an.automation.selenium.ExtendedWebDriver;

public class LoginPage extends ExtendedWebDriver{

	private String tfUserName = "xpath=//input[@name='uid'] --- tf UserName";
	private String tfPassword =  "xpath=//input[@name='password'] --- tf Password";
	private String btnLogin = "xpath=//input[@name='btnLogin'] --- btn Submit";
	
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public void inputUserName(String username) throws Exception {
		sendkeys(tfUserName, username);
	}
	
	public void inputPass(String pass) throws Exception {
		sendkeys(tfPassword, pass);
	}
	
	public void submitLogin() throws Exception {
		click(btnLogin);
	}	
	
	public void login(String username, String password) throws Exception {
		inputUserName(username);
		inputPass(password);
		submitLogin();
	}
}
