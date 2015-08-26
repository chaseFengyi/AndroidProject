package com.mynutritionstreet.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class MultipartRequest extends Request<String> {

	private Map<String, String> rawParams = null;
	private Response.Listener<String> mListener;

	public MultipartRequest(int post, String url,
			Map<String, String> rawParams, Listener<String> listener,
			ErrorListener errorListener) {
		// TODO Auto-generated constructor stub
		super(url,errorListener);
	}

	static MultipartEntity mMultiPartEntity = new MultipartEntity();

	/**
	 * @return
	 */
	public static MultipartEntity getMultiPartEntity() {
		return mMultiPartEntity;
	}

	@Override
	public String getBodyContentType() {
		return mMultiPartEntity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			// 将mMultiPartEntity中的参数写入到bos中
			mMultiPartEntity.writeTo(bos);
		} catch (IOException e) {
			Log.e("", "IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));

	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}

}
