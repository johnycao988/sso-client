package com.cs.auth.adapter.keycloak;

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
import com.cly.comm.util.IDUtil;
import com.cly.comm.util.JSONUtil;
import com.cs.auth.base.AuthException;
import com.cs.auth.base.AuthMgr;
import com.cs.auth.base.AuthSession;
import com.cs.auth.base.Token;

import net.iharder.Base64;
import net.sf.json.JSONObject;

public class KeycloakSession extends AuthSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 


	@Override
	public Token getUserAuthencatedToken() {

		String code = req.getParameter(KecycloakConst.CODE);
		
		String sessionState = req.getParameter(KecycloakConst.SESSION_STATE);

		if (code != null && sessionState != null) {
			codeToToken(code, sessionState);
		}else{
			
			
		}
		
		return null;

	}
	
	private Token codeToToken(String code, String sessionState) throws AuthException {
		
		try{
		
		HttpRequestParam hp = new HttpRequestParam();
		
		String clientBasicToken=IDUtil.Base64Encode(this.authProp.getClientId()+":"+this.authProp.getClientSecret());
		
		hp.addHeader(KecycloakConst.REQ_HEADER_AUTHORIZATION, KecycloakConst.REQ_HEADER_VALUE_BAISC + clientBasicToken);
		hp.addHeader(KecycloakConst.REQ_HEADER_CONTENT_TYPE, KecycloakConst.REQ_HEADER_VALUE_CONTENT_TYPE_FORM_URL_ENCODEED);
		hp.addParam(KecycloakConst.REQ_HEADER_GRANT_TYPE, KecycloakConst.REQ_HEADER_VALUE_AUTHORIZATION_CODE);
		hp.addParam(KecycloakConst.CLIENT_ID,this.authProp.getClientId() );
		hp.addParam(KecycloakConst.CLIENT_SECRET,this.authProp.getClientSecret());
		hp.addParam(KecycloakConst.CODE, code);
		hp.addParam(KecycloakConst.REDIRECT_URL,this.authProp.getClientRedirectRootUrl()+ req.getRequestURI());

		String reqUrl = this.authProp.getAuthServerRootUrl()+this.authProp.getTokenUri();

		String sr = HttpClient.request(reqUrl, HttpClient.REQUEST_METHOD_POST, hp);

		JSONObject jo = JSONObject.fromObject(sr);

		String st = JSONUtil.getString(jo, "access_token");
	
		return null;
		}catch(Exception e){
			throw new AuthException (e);
		}
	}

	private void getUserToken(String code, String sessionState) {

		HttpRequestParam hp = new HttpRequestParam();
		hp.addHeader("Authorization", "Basic " + csb);
		hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
		hp.addParam("grant_type", "authorization_code");
		hp.addParam("client_id", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID"));
		hp.addParam("client_secret", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET"));
		hp.addParam("code", authCode);
		hp.addParam("redirect_uri",
				AuthMgr.configProperties.get("KEYCLOAK.CLIENT.REDIRECT.ROOT.URL") + req.getRequestURI());

		String url = AuthMgr.configProperties.getProperty("KEYCLOAK.AUTH.SERVER.ROOT.URL")
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

		String sd = null;

		if (css != null)
			for (Cookie c : css) {
				String cn = c.getName();
				if (cn.equals(KecycloakConst.KEYCLOAK_COOKIES_SESSION_DATA)) {
					sd = c.getValue();
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
