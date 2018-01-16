package com.cs.auth.keycloak;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

			setCookieAuth(req, res);

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

	private void setCookieAuth(HttpServletRequest req, HttpServletResponse res) {

		authCode = req.getParameter("code");

		sessionState = req.getParameter("session_state");

		if (authCode != null && sessionState != null) {

			Cookie cookie = new Cookie(KEYCLOAK_AUTH_CODE, authCode);

			cookie.setMaxAge(-1);

			res.addCookie(cookie);

			cookie = new Cookie(KEYCLOAK_SESSION_STATE, sessionState);

			cookie.setMaxAge(-1);

			res.addCookie(cookie);

		}

	}

	public String getCode() {
		return authCode;
	}

	public String getSessionState() {
		return sessionState;
	}

}
