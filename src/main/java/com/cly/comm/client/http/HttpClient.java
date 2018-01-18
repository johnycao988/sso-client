package com.cly.comm.client.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

	public final static String REQUEST_METHOD_POST = "POST";
	
	public final static String REQUEST_METHOD_GET = "GET";

	private HttpClient() {

	} 
	 
	public static String request(String strUrl, String requestMethod, HttpRequestParam rp) throws IOException {
		return doHttpPost(strUrl, rp, requestMethod, true);
	}

 
	public static void noResponseRequest(String strUrl, String requestMethod,HttpRequestParam rp) throws IOException {
		doHttpPost(strUrl, rp, requestMethod, false);
	}

	public static InputStream getInputStream(String strUrl, String requestMethod, HttpRequestParam rp) throws IOException {

		HttpURLConnection conn = getConn(strUrl, rp, requestMethod, true);

		return conn.getInputStream();

	}

	 

	private static HttpURLConnection getConn(String strUrl, HttpRequestParam rp, String reqMethod, boolean hasResponse)
			throws IOException {

		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		String[] hds=rp.getHeaderNames();
		for(String hd:hds){
			conn.setRequestProperty(hd, rp.getHeaderValue(hd));
		}
		
		conn.setRequestMethod(reqMethod);
		if (hasResponse) {

			conn.setDoInput(true);

			int connTimeout = 30 * 1000;
			int readTimeout = 30 * 1000;

			if (rp != null) {
				connTimeout = rp.getConnectionTimeout();
				readTimeout = rp.getReadTimeout();
			}

			conn.setConnectTimeout(connTimeout);
			conn.setReadTimeout(readTimeout);
		} else {
			conn.setDoInput(false);
			conn.setConnectTimeout(500);
			conn.setReadTimeout(500);
		}

		if (rp != null) {

			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(rp.getParams().getBytes("UTF-8"));
			os.flush();
			os.close();
		} else
			conn.setDoOutput(false);

		return conn;
	}

	private static String doHttpPost(String strUrl, HttpRequestParam rp, String reqMethod, boolean hasResponse)
			throws IOException {

		HttpURLConnection conn = getConn(strUrl, rp, reqMethod, hasResponse);

		if (!hasResponse)
			return null;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line;
			String result = "";

			while ((line = br.readLine()) != null) {

				if (result.length() > 0)
					result += "\r\n" + line;

				else
					result = line;
			}

			br.close();

			return result;
		}

	}

	
	
	/**
	 * 
	 * public static String doHttpsPost(String strUrl, ReqParam rp) throws
	 * IOException {
	 * 
	 * return null; }
	 * 
	 * public static void doHttpsPostNoResponse(String strUrl, ReqParam rp)
	 * throws IOException {
	 * 
	 * }
	 */

}
