package com.mynutritionstreet.util;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;


public class HttpUtil extends StringRequest {

	private Map<String, String> rawParams = null;
	
	public HttpUtil(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	public HttpUtil(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

	public HttpUtil(String url, Map<String, String> rawParams,
			Listener<String> listener, ErrorListener errorListener) {
		super(Method.POST, url, listener, errorListener);
		this.rawParams = rawParams;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return rawParams;
	}

}
