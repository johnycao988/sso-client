package com.cs.auth.keycloak;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cs.auth.AuthServletFilter;

public class KeycloakServletFilter extends AuthServletFilter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (!validateSession(request, response))
			return;

		chain.doFilter(request, response);

	}

	private boolean validateSession(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		KeycloakSession ks =KeycloakSession.getKeyCloakSession(req, res);

		if (ks.isNeedLogin()) {
			ks.forwardLoginPage(request, response);
			return false;
		}

		return true;

	}

	
 
	/*
	 * public static String getToken() throws ClientProtocolException,
	 * IOException {
	 * 
	 * CloseableHttpClient hc = HttpClients.createDefault();
	 * 
	 * HttpPost httppost = new HttpPost(AUTH_SERVER + TOKEN_REQ_URL);
	 * 
	 * List<BasicNameValuePair> reqParams = new ArrayList<BasicNameValuePair>();
	 * 
	 * reqParams.add(new BasicNameValuePair("grant_type",
	 * "client_credentials")); reqParams.add(new BasicNameValuePair("client_id",
	 * CLIENT_ID)); reqParams.add(new BasicNameValuePair("client_secret",
	 * CLIENT_SECRET));
	 * 
	 * UrlEncodedFormEntity uefEntity;
	 * 
	 * uefEntity = new UrlEncodedFormEntity(reqParams, "UTF-8");
	 * httppost.setEntity(uefEntity); httppost.setHeader("Content-Type",
	 * "application/x-www-form-urlencoded");
	 * 
	 * CloseableHttpResponse res = hc.execute(httppost);
	 * 
	 * return EntityUtils.toString(res.getEntity());
	 * 
	 * }
	 * 
	 */

}
