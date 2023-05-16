package com.an.automation.report.markuphelper;

import java.io.IOException;
import java.util.List;

import com.an.automation.report.Log;
import com.an.automation.utility.Common;
import com.aventstack.extentreports.markuputils.Markup;

import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class APIRequestStep implements Markup {

	private Request request;
	private Response response;
	private Throwable exception;
	private String responseBody;
	private List<String> assertionList;

	public APIRequestStep(Request request, Response response, String responseBody,List<String> assertionList, Throwable exception) {
		this.request = request;
		this.response = response;
		this.assertionList = assertionList;
		this.exception = exception;
		this.responseBody = responseBody;
	}

	@Override
	public String getMarkup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='api-title' onclick='toggleDetail(this)'>");
		sb.append("<span class='" + request.method().toLowerCase() + "'>" + request.method() + "</span> ");
		sb.append("<a class='" + getAPIResult() + "'>" + request.url() + "</a>");
		sb.append("</div>");
		sb.append("<div class='api-detail' style='display:none;'>");
		sb.append("<div class='tab'>");
		sb.append("<button class='tablinks' onclick='openBlock(event,\"request\")'>Request</button>");
		sb.append("<button class='tablinks' onclick='openBlock(event,\"response\")'>Response</button>");
		sb.append("<button class='tablinks' onclick='openBlock(event,\"assertion\")'>Assertion</button>");
		sb.append("<button class='tablinks' onclick='openBlock(event,\"extract\")'>Extract</button>");
		sb.append("<button class='tablinks' onclick='openBlock(event,\"exception\")'>Exception</button>");
		sb.append("</div>");

		//// tab request
		sb.append("<div class='tabcontent request'>");
		sb.append("<span>Request Header: </span>");
		sb.append(getHeader());
		sb.append("<span>Request Body: </span>");
		sb.append("<div class='api-detail-block'>");
		sb.append("<span>");
		try {
			sb.append(getRequestBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("</span>");
		sb.append("</div>");
		sb.append("</div>");

		// tab response
		sb.append("<div class='tabcontent response'>");
		sb.append("<span>Response code: </span>");
		sb.append("<div class='api-detail-block'>");
		sb.append("<span>");
		if (response != null) {
			sb.append(response.code());
		}
		sb.append("</span>");
		sb.append("</div>");
		sb.append("<span>Response Body: </span>");
		sb.append("<div class='api-detail-block'>");
		sb.append("<span>");
		try {
			sb.append(getResponseBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("</span>");
		sb.append("</div>");
		sb.append("</div>");

		// tab assertion
		sb.append("<div class='tabcontent assertion'>");
		for (String assertNode : assertionList) {
			sb.append("<div class='api-detail-block'>");
			sb.append("<span> " + assertNode + " </span>");
			sb.append("</div>");
		}
		sb.append("</div>");

		// tab extract
		sb.append("<div class='tabcontent extract'>");
		sb.append("</div>");

		// tab exception
		sb.append("<div class='tabcontent exception'>");
		sb.append("<div class='api-detail-block'>");
		if (exception != null) {
			try {
				sb.append(Common.throwableToString(exception));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	public String getHeader() {
		StringBuilder sb = new StringBuilder();
		for (String name : request.headers().names()) {
			sb.append("<div class='api-detail-block'>");
			sb.append("<span>");
			sb.append(name + ": " + request.headers().get(name));
			sb.append("</span>");
			sb.append("</div>");
		}
		return sb.toString();
	}

	public String getRequestBody() throws Exception {
		try {
			StringBuilder sb = new StringBuilder();
			if (request.method().equalsIgnoreCase("post") || request.method().equalsIgnoreCase("put")) {
				try {
					Buffer buffer = new Buffer();
					request.body().writeTo(buffer);
					return buffer.readUtf8();
				} catch (IOException e) {
					return "Cannot get request body";
				}
			}
			return sb.toString();
		} catch (Exception e) {
			Log.error("Cannot get request Entity");
			throw e;
		}
	}

	public String getResponseBody() throws Exception {
		return this.responseBody;
	}
	
	public String getAPIResult() {
		for (String assertNode : assertionList) {
			if (assertNode.contains("FAILED")) {
				return "api-failed";
			}
		}
		if(exception != null) return "api-failed";
		return "api-passed";
	}

}
