package com.cs.auth.keycloak;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cly.comm.client.http.HttpClient;
import com.cly.comm.client.http.HttpRequestParam;
import com.cly.comm.util.JSONUtil;
import com.cs.auth.AuthMgr;

import net.iharder.Base64;
import net.sf.json.JSONObject;

public class KeyCloakSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String authCode;
	private String sessionState;
	private static final String KEYCLOAK_SESSION_STATE = "KEYCLOAK_SESSION_STATE";
	private static final String KEYCLOAK_AUTH_CODE = "KEYCLOAK_AUTH_CODE";

	public KeyCloakSession(HttpServletRequest req, HttpServletResponse res) {

		getCookieAuth(req);

		if (authCode == null || sessionState == null) {

		//	setCookieAuth(req, res);

		}

	}

	private void getCookieAuth(HttpServletRequest req) {

		Cookie[] cs = req.getCookies();

		if (cs != null)
			for (Cookie c : cs) {
				String cn = c.getName();
				if (cn.equals(KEYCLOAK_SESSION_STATE))
					sessionState = c.getValue();
				else if (cn.equals(KEYCLOAK_AUTH_CODE))
					authCode = c.getValue();
			}

	}

//	private void setCookieAuth(HttpServletRequest req, HttpServletResponse res) {
//
//		authCode = req.getParameter("code");
//
//		sessionState = req.getParameter("session_state");
//
//		if (authCode != null && sessionState != null) {
//
//			
//			authCode=getAuthorizationToken(req,res);
//			
//			
//			Cookie cookie = new Cookie(KEYCLOAK_AUTH_CODE, authCode);
//
//			cookie.setMaxAge(-1);
//
//			res.addCookie(cookie);
//
//			cookie = new Cookie(KEYCLOAK_SESSION_STATE, sessionState);
//
//			cookie.setMaxAge(-1);
//
//			res.addCookie(cookie); 
// 
//		} 
//
//	}
//	
//	private String getAuthorizationToken(HttpServletRequest req, HttpServletResponse res) {
//
//		try {
//
//			String cs = AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID") + ":"
//					+ AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET");
//
//			String csb = Base64.encodeBytes(cs.getBytes()).toString();
//
//			System.out.println("Client/Sceret:" + cs + "\r\n Client Basic Token:" + csb);
//
//			HttpRequestParam hp = new HttpRequestParam();
//			hp.addHeader("Authorization", "Basic "+csb);
//			hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
//			hp.addParam("grant_type", "authorization_code");
//		//	hp.addParam("client_id", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID"));
//		//	hp.addParam("client_secret", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET"));
//			hp.addParam("code", authCode);
//			hp.addParam("redirect_uri", AuthMgr.configProperties.get("KEYCLOAK.CLIENT.REDIRECT.ROOT.URL") + req.getRequestURI());
//			
//			String url = AuthMgr.configProperties.getProperty("KEYCLOAK.AUTH.SERVER.ROOT.URL")
//					+ AuthMgr.configProperties.getProperty("KEYCLOAK.TOKEN.URL");
//
//			String sr = HttpClient.request(url, HttpClient.REQUEST_METHOD_POST, hp);
//
//			JSONObject jo = JSONObject.fromObject(sr);
//
//			String st = JSONUtil.getString(jo, "access_token");
//
//			System.out.println("AAT Token:\r\n" + st);
//
//			return st;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//
//	} 

	public String getCode() {
		return authCode;
	}

	public String getSessionState() {
		return sessionState;
	}

}
