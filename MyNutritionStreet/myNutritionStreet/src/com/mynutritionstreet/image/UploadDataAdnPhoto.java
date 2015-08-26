package com.mynutritionstreet.image;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * 
 * @author FengYi~
 * 
 */
public class UploadDataAdnPhoto {
	/**
	 * post��ʽ�ϴ������������ļ�����
	 * @param url
	 * @param params
	 *            ����
	 * @param files
	 *            ͼƬ
	 * @return ���������ص�����
	 */
	public static String post(String url, Map<String, String> params,
			Map<String, File> files) {
		String BOUNDARY = java.util.UUID.randomUUID().toString();// �߽��ʶ �������
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPAR_FROM_DATA = "multipart/form-data";// ��������
		String CHARSET = "UTF-8";

		try {
			URL uri = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) uri
					.openConnection();
			connection.setReadTimeout(10 * 1000);// ������ʱ��
			connection.setConnectTimeout(10000);
			connection.setDoInput(true);// ��������
			connection.setDoOutput(true);// �������
			connection.setUseCaches(false);// ������ʹ�û���
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Content-Type", MULTIPAR_FROM_DATA
					+ ";boundary=" + BOUNDARY);

			// ��ƴ�ı����Ͳ���
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.write(sb.toString().getBytes());
			
			//�����ļ�����
			if(files != null){
				for(Map.Entry<String, File> file : files.entrySet()){
					System.out.println("exist-----------");
					StringBuilder sb1 = new StringBuilder();
	                sb1.append(PREFIX);
	                sb1.append(BOUNDARY);
	                sb1.append(LINEND);
	                sb1.append("Content-Disposition: form-data; name=\"imageFile\"; filename=\""
	                        + file.getValue() + "\"" + LINEND);
	                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
	                sb1.append(LINEND);
	                outputStream.write(sb1.toString().getBytes());
	                
	                InputStream is = new FileInputStream(file.getValue());
	                byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = is.read(buffer)) != -1) {
	                    outputStream.write(buffer, 0, len);
	                }

	                is.close();
	                outputStream.write(LINEND.getBytes());

				}
				
				// ���������־
		        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		        outputStream.write(end_data);
		        outputStream.flush();
		        // �õ���Ӧ��
		        int res = connection.getResponseCode();
		        InputStream in = connection.getInputStream();
		        StringBuilder sb2 = new StringBuilder();
		        if (res == 200) {
		            int ch;
		            while ((ch = in.read()) != -1) {
		                sb2.append((char) ch);
		            }
		        }
		        outputStream.close();
		        connection.disconnect();
		        return sb2.toString(); 
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �������ȡͼƬ
	 * @param imageUrl ͼƬ·��
	 * @param params ͼƬ��
	 * @return
	 */
	public static Bitmap loadImage(String imageUrl,List<NameValuePair> params){
		HttpPost httpPost = new HttpPost(imageUrl);
		Bitmap bitmap = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient()
			.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				 //ȡ�������Ϣ ȡ��HttpEntiy  
                HttpEntity httpEntity = httpResponse.getEntity();
                //���һ�������� 
                InputStream inputStream = httpEntity.getContent();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
			}
			return bitmap;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	
}
