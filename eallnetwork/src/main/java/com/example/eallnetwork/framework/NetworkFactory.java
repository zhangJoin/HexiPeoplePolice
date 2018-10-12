package com.example.eallnetwork.framework;

import java.util.HashMap;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public abstract class NetworkFactory {
    public static final int METHOD_GET = 0x1001, METHOD_POST = 0x1002;
    private SuccessfulCallback successfulCallback;
    private FailCallback failCallback;
    private int method;
    private String url;
    private HashMap<String, String> params;

    {
        method = METHOD_GET;
    }


    public void method(int method) {
        this.method = method;

    }

    ;

    public int getMethod() {
        return method;
    }

    public void url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public abstract void start();

    public void params(HashMap<String, String> map) {
        this.params = map;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void successCallback(SuccessfulCallback call) {
        successfulCallback = call;
    }

    public void failCallback(FailCallback call) {
        failCallback = call;
    }

    public SuccessfulCallback getSuccessfulCallback() {
        return successfulCallback;
    }

    public FailCallback getFailCallback() {
        return failCallback;
    }


    public String getGetParams(HashMap<String, String> map) {
        if (map == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Set<String> set = map.keySet();
        for (String k : set) {
            builder.append(k);
            builder.append("=");
            builder.append(map.get(k));
            builder.append("&");
        }
        return builder.substring(0, builder.length());
    }


//public RequestBody getPostParams(HashMap<String, String> map){
//	FormEncodingBuilder postParams=new FormEncodingBuilder();
//	Set<String> set=map.keySet();
//	for(String k:set){
//		postParams.add(k, map.get(k));
//	}
//
//	return postParams.build();
//}

    public RequestBody getPostParams(HashMap<String, String> map) {

        FormBody.Builder formBody = new FormBody.Builder();
        Set<String> set = map.keySet();
        for (String k : set) {
            if (map.get(k) != null && !map.get(k).equals("")) {
                formBody.add(k, map.get(k));
            }
//		 else{
//			 formBody.add(k, "");
//		 }
        }

        return formBody.build();
    }

    public void start(int method, String url, HashMap<String, String> params, SuccessfulCallback successCall, FailCallback failsCall) {

    }


}
