package com.example.eallnetwork.framework;

import org.json.JSONException;

import java.io.InputStream;

public interface SuccessfulCallback {
	
	public void success(String str) throws JSONException;
	public void success(InputStream ism, long conentLength);

}
