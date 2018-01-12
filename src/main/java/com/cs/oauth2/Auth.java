package com.cs.oauth2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Auth {

	private static String TOKEN_AUTH_URL = "";
	private static String CLIENT_ID = "";
	private static String CLIENT_REDIRECT_ROOT_URL = "";
	private static String CLIENT_SECRET = "";
	private static String TOKEN_REQ_URL = "";

	private static String AUTH_SERVER = init();

	private static String init() {

		try {
			Properties p = new Properties();

			p.load(Auth.class.getResourceAsStream("/config/config.properties"));

			AUTH_SERVER = p.getProperty("AUTH_SERVER");
			TOKEN_AUTH_URL = p.getProperty("TOKEN_AUTH_URL");
			CLIENT_ID = p.getProperty("CLIENT_ID");
			CLIENT_REDIRECT_ROOT_URL = p.getProperty("CLIENT_REDIRECT_ROOT_URL");
			CLIENT_SECRET = p.getProperty("CLIENT_SECRET");
			TOKEN_REQ_URL = p.getProperty("TOKEN_REQ_URL");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return AUTH_SERVER;

	}

	public static boolean checkSession(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;

		if (req.getSession(false) != null)
			return true;
		else
			req.getSession(true);

		forwardLoginPage(request, response);

		return false;

	}

	public static void forwardLoginPage(ServletRequest request, ServletResponse response) throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String reqLoginUrl = AUTH_SERVER + TOKEN_AUTH_URL + "?client_id=" + CLIENT_ID
				+ "&response_type=code&scope=openid&redirect_uri=" + CLIENT_REDIRECT_ROOT_URL + req.getRequestURI();

		System.out.println(reqLoginUrl);

		res.sendRedirect(reqLoginUrl);

	}

	public static String getToken() throws ClientProtocolException, IOException {

		CloseableHttpClient hc = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(AUTH_SERVER + TOKEN_REQ_URL);

		List<BasicNameValuePair> reqParams = new ArrayList<BasicNameValuePair>();

		reqParams.add(new BasicNameValuePair("grant_type", "client_credentials"));
		reqParams.add(new BasicNameValuePair("client_id", CLIENT_ID));
		reqParams.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));

		UrlEncodedFormEntity uefEntity;

		uefEntity = new UrlEncodedFormEntity(reqParams, "UTF-8");
		httppost.setEntity(uefEntity);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		CloseableHttpResponse res = hc.execute(httppost);

		return EntityUtils.toString(res.getEntity());

	}

}