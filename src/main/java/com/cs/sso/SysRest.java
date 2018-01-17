package com.cs.sso;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cly.comm.client.http.HttpClient;
import com.cly.comm.client.http.HttpRequestParam;
import com.cly.comm.util.JSONUtil;
import com.cs.auth.AuthException;
import com.cs.auth.AuthMgr;
import com.cs.auth.keycloak.KeyCloakSession;
import com.cs.auth.keycloak.KeycloakToken;

import net.iharder.Base64;
import net.sf.json.JSONObject;

@Controller
@RestController
public class SysRest {

	@RequestMapping(value = { "/", "/index", "/welcome", "version", "info" }, method = RequestMethod.GET)
	public String getVersion() {

		return "SSO Client - version v1.0";
	}

//	@RequestMapping("/token")
//	public String getToken(HttpServletRequest req, HttpServletResponse res) throws AuthException, IOException {
//
//		KeyCloakSession ks = new KeyCloakSession(req, res);
//		String ut = ks.getCode();
//		System.out.println("User Authentication Token:\r\n" + ut);
//
//		getAuthorizationToken(ut);
//
//		String pt = getPermissionToken();
//
//		return "OK";
//
//	}

//	private String getPermissionToken() {
//
//		try {
//			HttpRequestParam hp = new HttpRequestParam();
//
//			hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
//			hp.addParam("grant_type", "client_credentials");
//			hp.addParam("client_id", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID"));
//			hp.addParam("client_secret", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET"));
//			String url = AuthMgr.configProperties.getProperty("KEYCLOAK.AUTH.SERVER.ROOT.URL")
//					+ AuthMgr.configProperties.getProperty("KEYCLOAK.TOKEN.URL");
//
//			String sr = HttpClient.request(url, HttpClient.REQUEST_METHOD_POST, hp);
//
//			JSONObject jo = JSONObject.fromObject(sr);
//
//			String st = JSONUtil.getString(jo, "access_token");
//
//			System.out.println("Client Permission Token:\r\n" + st);
//
//			return st;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//
//	}
//
//	private String getAuthorizationToken(String code) {
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
//			hp.addHeader("Authorization", "Basic "+csb+"aa");
//			hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
//			hp.addParam("grant_type", "authorization_code");
//		//	hp.addParam("client_id", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.ID"));
//		//	hp.addParam("client_secret", AuthMgr.configProperties.getProperty("KEYCLOAK.CLIENT.SECRET"));
//			hp.addParam("code", code);
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

}
