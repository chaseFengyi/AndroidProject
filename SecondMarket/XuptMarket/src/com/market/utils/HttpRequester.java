package com.market.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;

import com.market.model.HttpResponsBean;

/**
 * HTTP�������
 * 
 * @author YYmmiinngg
 */
public class HttpRequester {
	private String defaultContentEncoding;

	public HttpRequester() {
		this.defaultContentEncoding = Charset.defaultCharset().name();
	}

	/**
	 * ����GET����
	 * 
	 * @param urlString
	 *            URL��ַ
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	public HttpResponsBean sendGet(String urlString, String SessionId)
			throws IOException {
		return this.send(urlString, "GET", null, null, SessionId);
	}

	/**
	 * ����GET����
	 * 
	 * @param urlString
	 *            URL��ַ
	 * @param params
	 *            ��������
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	public HttpResponsBean sendGet(String urlString, Map<String, String> params,
			String SessionId) throws IOException {
		return this.send(urlString, "GET", params, null, SessionId);
	}

	/**
	 * ����GET����
	 * 
	 * @param urlString
	 *            URL��ַ
	 * @param params
	 *            ��������
	 * @param propertys
	 *            ��������
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	public HttpResponsBean sendGet(String urlString, Map<String, String> params,
			Map<String, String> propertys, String SessionId) throws IOException {
		return this.send(urlString, "GET", params, propertys, SessionId);
	}

	/**
	 * ����POST����
	 * 
	 * @param urlString
	 *            URL��ַ
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	public HttpResponsBean sendPost(String urlString, String SessionId)
			throws IOException {
		return this.send(urlString, "POST", null, null, SessionId);
	}

	/**
	 * ����POST����
	 * 
	 * @param urlString
	 *            URL��ַ
	 * @param params
	 *            ��������
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	public HttpResponsBean sendPost(String urlString, Map<String, String> params,
			String SessionId) throws IOException {
		return this.send(urlString, "POST", params, null, SessionId);
	}

	/**
	 * ����POST����
	 * 
	 * @param urlString
	 *            URL��ַ
	 * @param params
	 *            ��������
	 * @param propertys
	 *            ��������
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	public HttpResponsBean sendPost(String urlString, Map<String, String> params,
			Map<String, String> propertys, String SessionId) throws IOException {
		return this.send(urlString, "POST", params, propertys, SessionId);
	}

	/**
	 * ����HTTP����
	 * 
	 * @param urlString
	 * @return ��ӳ����
	 * @throws IOException
	 */
	private HttpResponsBean send(String urlString, String method,
			Map<String, String> parameters, Map<String, String> propertys,
			String SessionId) throws IOException {
		HttpURLConnection urlConnection = null;

		if (method.equalsIgnoreCase("GET") && parameters != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i == 0)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			urlString += param;
		}
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection();
		if (!(SessionId == null)) {
			urlConnection.setRequestProperty("Cookie", SessionId);
		}

		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);

		if (propertys != null)
			for (String key : propertys.keySet()) {
				urlConnection.addRequestProperty(key, propertys.get(key));
			}

		if (method.equalsIgnoreCase("POST") && parameters != null) {
			StringBuffer param = new StringBuffer();
			for (String key : parameters.keySet()) {
				param.append("&");
				param.append(key).append("=").append(parameters.get(key));
			}
			urlConnection.getOutputStream().write(param.toString().getBytes());
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();
		}

		return this.makeContent(urlString, urlConnection);
	}

	/**
	 * �õ���Ӧ����
	 * 
	 * @param urlConnection
	 * @return ��Ӧ����
	 * @throws IOException
	 */
	private HttpResponsBean makeContent(String urlString,
			HttpURLConnection urlConnection) throws IOException {
		HttpResponsBean httpResponser = new HttpResponsBean();
		try {
			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			httpResponser.contentCollection = new Vector<String>();
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				httpResponser.contentCollection.add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			String ecod = urlConnection.getContentEncoding();
			if (ecod == null)
				ecod = this.defaultContentEncoding;

			httpResponser.setUrlString(urlString);

			httpResponser.setDefaultPort(urlConnection.getURL()
					.getDefaultPort());
			httpResponser.setFile(urlConnection.getURL().getFile());
			httpResponser.setHost(urlConnection.getURL().getHost());
			httpResponser.setPath(urlConnection.getURL().getPath());
			httpResponser.setPort(urlConnection.getURL().getPort());
			httpResponser.setProtocol(urlConnection.getURL().getProtocol());
			httpResponser.setQuery(urlConnection.getURL().getQuery());
			httpResponser.setRef(urlConnection.getURL().getRef());
			httpResponser.setUserInfo(urlConnection.getURL().getUserInfo());

			httpResponser.setContent(new String(temp.toString().getBytes(),
					ecod));
			httpResponser.setContentEncoding(ecod);
			httpResponser.setCode(urlConnection.getResponseCode());
			httpResponser.setMessage(urlConnection.getResponseMessage());
			httpResponser.setContentType(urlConnection.getContentType());
			httpResponser.setMethod(urlConnection.getRequestMethod());
			httpResponser.setConnectTimeout(urlConnection.getConnectTimeout());
			httpResponser.setReadTimeout(urlConnection.getReadTimeout());
			httpResponser.setSessionId(urlConnection
					.getHeaderField("Set-Cookie"));

			return httpResponser;
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	/**
	 * Ĭ�ϵ���Ӧ�ַ���
	 */
	public String getDefaultContentEncoding() {
		return this.defaultContentEncoding;
	}

	/**
	 * ����Ĭ�ϵ���Ӧ�ַ���
	 */
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}
}