package com.an.automation.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.an.automation.report.Log;
import com.an.automation.utility.JsonParser;

import okhttp3.Response;

public class APIRequestVerification<T extends APIRequestVerification<T>> {

	protected Response response;

	protected String responseBody;
	protected JSONObject responseBodyInJson;
	protected int statusCode;

	protected List<String> assertionList;
	protected Exception exception;

	public void initResponseValidation() throws IOException {
		try {
			responseBody = response.body().string();
			statusCode = response.code();
			Log.info("Response: [" + statusCode + "] [" + responseBody.replaceAll("\\r|\\n|\\t|\t", "") + "]");
			if (responseBody.startsWith("[")) {
				responseBodyInJson = JsonParser.convetToJsonArray(responseBody).getJSONObject(0);
			} else if (responseBody.startsWith("{")) {
				responseBodyInJson = JsonParser.convetToJsonObject(responseBody);
			} else {
				responseBodyInJson = null;
			}
			assertionList = new ArrayList<String>();
			exception = null;
		} catch (IOException e) {
			exception = e;
		}
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public JSONObject getResponseBodyInJson() {
		return this.responseBodyInJson;
	}

	public String getStringResponse() throws IOException {
		return responseBody;
	}
	
	private T extracted() {
		return (T)this;
	}

	public String getValueFromResponse(String keychains) throws Exception {
		JsonParser jsonParser = new JsonParser(responseBodyInJson);
		return jsonParser.extractJsonValue(keychains);
	}

	public T withValidation() {
		return extracted();
	}

	public T responseCodeShouldBe(int code) {
		boolean validate = getStatusCode() == code;
		if (validate) {
			assertionList.add("[PASSED] Response code is [" + code + "]");
			Log.info("[Assertion][PASSED] Response code is [" + code + "]");
		} else {
			assertionList.add("[FAILED] Response code is not [" + code + "]");
			Log.error("[Assertion][FAILED] Response code is not [" + code + "]");
		}
		return extracted();
	}

	

	public T responseBodyShouldContain(String expectedValue) throws Exception {
		if (responseBody.contains(expectedValue)) {
			assertionList.add("[PASSED] Response body contains [" + expectedValue + "]");
			Log.info("[Assertion][PASSED] Response body contains [" + expectedValue + "]");
		} else {
			assertionList.add("[FAILED] Response body doesnt contain [" + expectedValue + "]");
			Log.error("[Assertion][FAILED] Response body doesnt contain [" + expectedValue + "]");
		}
		return extracted();
	}

	public T responseBodyShouldEquals(String expectedValue) throws Exception {
		if (responseBody.equals(expectedValue)) {
			assertionList.add("[PASSED] Response body is equal [" + expectedValue + "]");
			Log.info("[Assertion][PASSED] Response body is equal [" + expectedValue + "]");
		} else {
			assertionList.add("[FAILED] Response body is not equal [" + expectedValue + "]");
			Log.error("[Assertion][FAILED] Response body is not equal [" + expectedValue + "]");
		}
		return extracted();
	}

}
