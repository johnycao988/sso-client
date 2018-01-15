package com.cs.auth.keycloak;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class KeyCloakSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String code;
	private final String sessionState;
	private final String jSessionId;
	public final static String JSESSION_ID = "JSESSIONID";

	public KeyCloakSession(HttpServletRequest req) {

		code = req.getParameter("code");
		sessionState = req.getParameter("session_state");
		jSessionId = getCookieSessionID(req);
		
		System.out.println("create Keycloak session ---");
		
		System.out.println("code:"+code);
		System.out.println("sessionState:"+sessionState);
		System.out.println("jSessionId:"+jSessionId);

		if (jSessionId != null && code != null && sessionState != null)
			req.getSession().setAttribute(JSESSION_ID, this);

	}

	private String getCookieSessionID(HttpServletRequest req) {

		Cookie[] cs = req.getCookies();

		if (cs != null)
			for (Cookie c : cs) {
				if (c.getName().equals(JSESSION_ID))
					return c.getValue();

			}

		return null;

	}

	public String getCode() {
		return code;
	}

	public String getSessionState() {
		return sessionState;
	}

	public String getJSessionId() {
		return jSessionId;
	}

}
