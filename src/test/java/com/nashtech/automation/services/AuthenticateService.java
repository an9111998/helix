package com.nashtech.automation.services;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.an.automation.api.APIRequest;
import com.an.automation.api.APIResponse;
import com.nashtech.automation.setup.EnvironmentVariables;

public class AuthenticateService {

	private static String loginPath = "/Api/Authentication/Authenticate";
	private static String businessPortalLoginPath = "/AdminApi/Login/Authenticate";


	public static APIRequest loginAdminPanelRequest(String username, String password) throws Exception {
		APIRequest request = new APIRequest();
		request.baseUrl(EnvironmentVariables.BASE_URL).path(loginPath).addParam("identifier", username)
				.addParam("password", password).addHeader("Content-Type", "application/json").post()
				.getResponse();
				
		return request;
	}
	
	public static APIRequest loginBusinessPortalRequest(String username, String password) throws Exception {
		APIRequest request = new APIRequest();
		String body = "Identifier=" + username + "&Password=" + password;
		request.baseUrl(EnvironmentVariables.BASE_URL).path(businessPortalLoginPath)
				//.addFormData("Identifier", username)
				//.addFormData("Password", password)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.body(body)
				.post()
				.getResponse();
				
		return request;
	}

}
