package com.cly.comm.client.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParam {

	private int connTimeout = 30 * 1000;

	private int readTimeout = 30 * 1000;
	
	public final static String REQ_JSON_MESSAGE_NAME="reqJsonMsg";

	private HashMap<String, String> hmParam = new HashMap<String, String>();
	
	private HashMap<String, String> hmHeader = new HashMap<String, String>();

	public void setConnectionTimeout(int timeout) {
		this.connTimeout = timeout;
	}

	public void setReadTimeout(int timeout) {
		this.readTimeout = timeout;
	}

	public int getConnectionTimeout() {
		return this.connTimeout;
	}

	public int getReadTimeout() {
		return this.readTimeout;
	}

	public void addParam(String name, Object value) {

		hmParam.put(name, (String) value);

	}
	
	public void addHeader(String name, String value) {

		hmHeader.put(name,  value);

	}
	
	public String getHeaderValue(String headerName){
		return hmHeader.get(headerName);
	}
	
	public String[] getHeaderNames() {
		
		ArrayList<String> al=new ArrayList<String>();
		

		for (Map.Entry<String, String> entry : hmHeader.entrySet()) {
			al.add(entry.getKey());
			 
		}
		
		return al.toArray(new String[0]);
		
	}
	
	

	public String getParams() {

		StringBuilder sb = new StringBuilder();

		int nums = 0;

		for (Map.Entry<String, String> entry : hmParam.entrySet()) {

			if (nums > 0)
				sb.append("&");

			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			nums = 1;
		}

		return sb.toString();

	}

	public String toString() {
		return getParams();
	}

}
