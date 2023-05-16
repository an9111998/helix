package com.nashtech.automation.tests;

import static com.an.automation.utility.Assertion.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nashtech.automation.pages.LoginPage;
import com.nashtech.automation.setup.WebTestSetup;

public class LoginTests extends WebTestSetup {

	@BeforeMethod
	public void setup() throws Exception {
		currentPage.openUrl("https://demo.guru99.com/v4/");
	}

	public void loginToHomePage() throws Exception {
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.login("mngr490801","rEzEtej");

	}

	@Test
	public void Verify_Admin_Can_Login_Into_Admin_Panel() throws Exception {
		loginToHomePage();
	}


}
