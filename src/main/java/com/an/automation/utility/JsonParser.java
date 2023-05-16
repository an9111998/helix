package com.an.automation.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {
	
	public enum OUTPUT_TYPE {
		INT,
		STRING,
		DOUBLE
	}
	
	private JSONObject json;

	public JsonParser(JSONObject json) {
		this.json = json;
	}
	
	public JsonParser() {
		// TODO Auto-generated constructor stub
	}

	public static JSONArray convetToJsonArray(String jsonString) {
		return new JSONArray(jsonString);
	}
	
	public static JSONObject convetToJsonObject(String jsonString) {
		return new JSONObject(jsonString);
	}

	public String extractJsonValue(String keychains) throws Exception {
		return extractToString(this.json, keychains);
	}
	
	public static String extractToString(JSONObject json, String keychains) throws Exception {
		return extractValue(json,keychains);
	}
	
	public static int extractToInt(JSONObject json, String keychains) throws Exception {
		String extractedValue = extractValue(json,keychains);
		return Integer.parseInt(extractedValue);
	}
	
	public static double extractToDouble(JSONObject json, String keychains) throws Exception {
		String extractedValue = extractValue(json,keychains);
		return Double.parseDouble(extractedValue);
	}
	
	public static String extractToString(String jsonString, String keychains) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		return extractValue(json,keychains);
	}
	
	public static int extractToInt(String jsonString, String keychains) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		String extractedValue = extractValue(json,keychains);
		return Integer.parseInt(extractedValue);
	}
	
	public static double extractToDouble(String jsonString, String keychains) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		String extractedValue = extractValue(json,keychains);
		return Double.parseDouble(extractedValue);
	}
	
	private static String extractValue(JSONObject json, String keychains) throws Exception {

		String errorMessage = "";

		if (json != null) {
			JSONObject currentNode = json;
			String[] keys = keychains.split("\\.");

			for (int i = 0; i < keys.length; i++) {
				String currentExtractKey = keys[i];
				String key = "";
				String indexInArray = StringUtils.substringBetween(currentExtractKey, "[", "]");

				if (indexInArray != null) {
					key = currentExtractKey.substring(0, currentExtractKey.indexOf("["));
				} else {
					key = currentExtractKey;
				}

				if (currentNode.has(key)) {

					Object extractedNode = currentNode.get(key);

					if ((indexInArray != null) && (extractedNode instanceof JSONArray)) {

						extractedNode = ((JSONArray) extractedNode).get(Integer.parseInt(indexInArray));

					} else if ((indexInArray != null) && !(extractedNode instanceof JSONArray)) {

						errorMessage = "[Extract][FAILED] [" + key + "] in [" + keychains + "] is not an json array";
						break;

					}

					if (i == keys.length - 1) {

						String extractedValue = extractedNode.toString();
						return extractedValue.equalsIgnoreCase("") || extractedValue.equalsIgnoreCase("null") ? ""
								: extractedValue;

					} else if (!(extractedNode instanceof JSONObject) && !(extractedNode instanceof JSONArray)) {

						errorMessage = "[Extract][FAILED] key [" + key + "] in [" + keychains + "] does not contain ["
								+ keys[i + 1] + "]";
						break;

					} else {

						currentNode = (JSONObject) extractedNode;

					}
				} else {

					errorMessage = "[FAILED] key [" + key + "] in [" + keychains + "] does not exist";
					break;
				}
			}

		} else {
			throw new Exception("Response is null!!!");
		}
		
		throw new Exception(errorMessage);
	}



}
