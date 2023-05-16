package com.an.automation.api;

import java.io.IOException;
import org.json.JSONObject;

import com.an.automation.utility.JsonParser;

import okhttp3.Response;

public class APIResponse {

	private String responseInString;
	private JSONObject responseInJsonObject;
	private int statusCode;

	public APIResponse(Response response, String responseBody) {
		responseInString = responseBody;
		statusCode = response.code();
		if (responseInString.startsWith("[")) {
			responseInJsonObject = JsonParser.convetToJsonArray(responseInString).getJSONObject(0);
		} else if (responseInString.startsWith("{")) {
			responseInJsonObject = JsonParser.convetToJsonObject(responseInString);
		} else {
			responseInJsonObject = null;
		}
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public JSONObject getResponseBodyInJson() {
		return this.responseInJsonObject;
	}

	public String getStringResponse() throws IOException {
		return responseInString;
	}

	public String getValueFromResponse(String keychains) throws Exception {
		JsonParser jsonParser = new JsonParser(responseInJsonObject);
		return jsonParser.extractJsonValue(keychains);
	}

}
