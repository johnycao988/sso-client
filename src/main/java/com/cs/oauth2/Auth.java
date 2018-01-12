package com.cs.oauth2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Auth {

	private static final Client client = new Client("sso.client.dev", "345bffd7-802b-46be-9def-2ae1386c6bc1");

	private static final String AUTH_SERVER = "http://sso.cscloud.com";

	private static final String TOKEN_REQ_URL = "/auth/realms/CSCloud/protocol/openid-connect/token";

	private static final String TOKEN_AUTH_URL = "/auth/realms/CSCloud/protocol/openid-connect/auth";
	
	private static final String CLIENT_REDIRECT_ROOT_URL = "http://localhost:3000";
	

	public static boolean needLogin(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		
		

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if(req.getSession(false)!=null)
			return false;
		else req.getSession(true);	
		
		
		String reqLoginUrl = AUTH_SERVER + TOKEN_AUTH_URL
				+ "?client_id=sso.client.dev&response_type=code&scope=openid&redirect_uri="+CLIENT_REDIRECT_ROOT_URL+req.getRequestURI();

		res.sendRedirect(reqLoginUrl);
		
		return true;

	}

	public static String getToken() throws ClientProtocolException, IOException {

		CloseableHttpClient hc = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(AUTH_SERVER + TOKEN_REQ_URL);

		List<BasicNameValuePair> reqParams = new ArrayList<BasicNameValuePair>();

		reqParams.add(new BasicNameValuePair("grant_type", "client_credentials"));
		reqParams.add(new BasicNameValuePair("client_id", client.getId()));
		reqParams.add(new BasicNameValuePair("client_secret", client.getSecret()));

		UrlEncodedFormEntity uefEntity;

		uefEntity = new UrlEncodedFormEntity(reqParams, "UTF-8");
		httppost.setEntity(uefEntity);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		CloseableHttpResponse res = hc.execute(httppost);

		return EntityUtils.toString(res.getEntity());

	}

}