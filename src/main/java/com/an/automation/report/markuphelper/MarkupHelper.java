package com.an.automation.report.markuphelper;

import java.util.List;

import com.aventstack.extentreports.markuputils.Markup;

import okhttp3.Request;
import okhttp3.Response;

public class MarkupHelper extends com.aventstack.extentreports.markuputils.MarkupHelper{
	
	public static Markup createAPIRequestStep(Request request, Response response,String responseBody,List<String> assertionList, Throwable exception) {
        return new APIRequestStep(request,response,responseBody,assertionList,exception);
    }

}
