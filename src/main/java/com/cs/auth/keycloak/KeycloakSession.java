package com.cs.auth.keycloak;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cly.comm.client.http.HttpClient;
import com.cly.comm.client.http.HttpRequestParam;
import com.cly.comm.util.JSONUtil;
import com.cs.auth.AuthMgr;

import net.iharder.Base64;
import net.sf.json.JSONObject;

public class KeycloakSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;
	private HttpServletResponse res;

	private KeycloakSession(HttpServletRequest req, HttpServletResponse res) {

		this.req = req;
		
		this.res = res;
		
	}

	public static KeycloakSession getKeyCloakSession(HttpServletRequest req, HttpServletResponse res){
		
		KeycloakSession ks=(KeycloakSession) req.getAttribute(KecycloakConst.KEYCLOAK_SESSION_NAME);
		
		if(ks==null){
			
			ks=new KeycloakSession(req,res);
			req.setAttribute(KecycloakConst.KEYCLOAK_SESSION_NAME, ks);
		}		
		return ks;
	}

	public boolean isNeedLogin() {
		
		initUserToken();
		
		String sd=this.getCookieSessionData();

	}
	
	private void initUserToken() throws AuthException{
		
		 String code = req.getParameter(KecycloakConst.CODE);
		 String sessionState = req.getParameter(KecycloakConst.SESSION_STATE);
		 
		 if(code!=null && sessionState!=null){
			 getUserToken(code, sessionState);
		 }
		 
		
	}
	
	private void getUserToken(String code, String sessionState){
		
		HttpRequestParam hp = new HttpRequestParam();
		hp.addHeader("Authorization", "Basic "+csb);
		hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
		hp.addParam("grant_type", "authorization_code");
		hp.addParam("client_id",
		AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID"));
		hp.addParam("client_secret",
		AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET"));
		hp.addParam("code", authCode);
		hp.addParam("redirect_uri",
		AuthMgr.configProperties.get("KEYCLOAK.CLIENT.REDIRECT.ROOT.URL") +
		req.getRequestURI());
		
		String url =
		 AuthMgr.configProperties.getProperty("KEYCLOAK.AUTH.SERVER.ROOT.URL")
		 + AuthMgr.configProperties.getProperty("KEYCLOAK.TOKEN.URL");
		
		 String sr = HttpClient.request(url, HttpClient.REQUEST_METHOD_POST, hp);
		
		 JSONObject jo = JSONObject.fromObject(sr);
		
		 String st = JSONUtil.getString(jo, "access_token");
		
		 System.out.println("AAT Token:\r\n" + st);
		
		 return st;
		
	}
	
	public void forwardLoginPage(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String reqLoginUrl = configProperties.getAuthServerRootUrl() + configProperties.getAuthUrl() + "?client_id="
				+ configProperties.getClientId() + "&response_type=code&scope=openid&redirect_uri="
				+ configProperties.getClientRedirectRootUrl() + req.getRequestURI();

		System.out.println("****** req login url:" + reqLoginUrl);

		res.sendRedirect(reqLoginUrl);

	}


	private String getCookieSessionData() {

		Cookie[] css = req.getCookies();
		
		String sd=null;

		if (css != null)
			for (Cookie c : css) {
				String cn = c.getName();
				if (cn.equals(KecycloakConst.KEYCLOAK_COOKIES_SESSION_DATA)) {
					sd=c.getValue();
					break;
				}
			}
		
		  return sd;

	}

	// private void setCookieAuth(HttpServletRequest req, HttpServletResponse
	// res) {
	//
	// authCode = req.getParameter("code");
	//
	// sessionState = req.getParameter("session_state");
	//
	// if (authCode != null && sessionState != null) {
	//
	//
	// authCode=getAuthorizationToken(req,res);
	//
	//
	// Cookie cookie = new Cookie(KEYCLOAK_AUTH_CODE, authCode);
	//
	// cookie.setMaxAge(-1);
	//
	// res.addCookie(cookie);
	//
	// cookie = new Cookie(KEYCLOAK_SESSION_STATE, sessionState);
	//
	// cookie.setMaxAge(-1);
	//
	// res.addCookie(cookie);
	//
	// }
	//
	// }
	//
	// private String getAuthorizationToken(HttpServletRequest req,
	// HttpServletResponse res) {
	//
	// try {
	//
	// String cs = AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID") +
	// ":"
	// + AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET");
	//
	// String csb = Base64.encodeBytes(cs.getBytes()).toString();
	//
	// System.out.println("Client/Sceret:" + cs + "\r\n Client Basic Token:" +
	// csb);
	//
	// HttpRequestParam hp = new HttpRequestParam();
	// hp.addHeader("Authorization", "Basic "+csb);
	// hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
	// hp.addParam("grant_type", "authorization_code");
	// // hp.addParam("client_id",
	// AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID"));
	// // hp.addParam("client_secret",
	// AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET"));
	// hp.addParam("code", authCode);
	// hp.addParam("redirect_uri",
	// AuthMgr.configProperties.get("KEYCLOAK.CLIENT.REDIRECT.ROOT.URL") +
	// req.getRequestURI());
	//
	// String url =
	// AuthMgr.configProperties.getProperty("KEYCLOAK.AUTH.SERVER.ROOT.URL")
	// + AuthMgr.configProperties.getProperty("KEYCLOAK.TOKEN.URL");
	//
	// String sr = HttpClient.request(url, HttpClient.REQUEST_METHOD_POST, hp);
	//
	// JSONObject jo = JSONObject.fromObject(sr);
	//
	// String st = JSONUtil.getString(jo, "access_token");
	//
	// System.out.println("AAT Token:\r\n" + st);
	//
	// return st;
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// return "";
	// }
	//
	// }

 

}
