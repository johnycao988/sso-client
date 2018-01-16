package com.cs.auth.keycloak;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class KeyCloakSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String code;
	private final String sessionState;
	private static final String KEY_CLOAK_SESSION = "KEY_CLOAK_SESSION";

	public KeyCloakSession(HttpServletRequest req) {

		code = req.getParameter("code");
		sessionState = req.getParameter("session_state");

		System.out.println("create Keycloak session ---");

		System.out.println("code:" + code);
		System.out.println("sessionState:" + sessionState);

		if (code != null && sessionState != null){			
			req.getSession().setAttribute(KEY_CLOAK_SESSION, this);			
		}

	}

	public static KeyCloakSession getKeyCloakSession(HttpServletRequest req) {

		return (KeyCloakSession) req.getSession().getAttribute(KEY_CLOAK_SESSION);

	}

	public String getCode() {
		return code;
	}

	public String getSessionState() {
		return sessionState;
	}

}
