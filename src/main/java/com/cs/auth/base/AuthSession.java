package com.cs.auth.base;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cly.comm.client.http.HttpUtil;
import com.cly.comm.util.IDUtil;

public abstract class AuthSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String AUTH_SESSION_NAME = "AUTH_SESSION_NAME";
	private final String AUTH_SESSION_ID = "AUTH.SESSION.ID";

	protected HttpServletRequest req;

	protected HttpServletResponse res;

	protected AuthProperties authProp;

	public abstract Token getUserAuthencatedToken() throws AuthException;

	public abstract void forwardLoginPage() throws AuthException;

	public abstract void refreshToken(Token token) throws AuthException; 

	
	public boolean needLogin() {

		try {

			Token userAuthToken = getUserAuthencatedToken();

			if (userAuthToken == null)
				return true;
			else if (!userAuthToken.isExpired())
				return false;
			else if (!userAuthToken.isRefreshExpired()) {
				this.refreshToken(userAuthToken);
				return false;
			} else
				return true;
		} catch (AuthException e) {
			AuthLogger.error(e);
			return false;
		}
	}

	public String getAuthSessionId() {
		return HttpUtil.getCookieValue(req, AUTH_SESSION_ID);
	}

	public void setAuthSessionId(String sessId) {

		HttpUtil.setCookieValue(res, AUTH_SESSION_ID, sessId, -1);

	}

	public String createAuthSessionId() {
		return IDUtil.getRandomBase64UUID();
	}

	public static AuthSession getAuthSession(HttpServletRequest req, HttpServletResponse res, Class<?> authClass,
			AuthProperties ap) throws AuthException {

		AuthSession as = (AuthSession) req.getAttribute(AUTH_SESSION_NAME);

		if (as == null) {
			as = createAuthSession(authClass);
			req.setAttribute(AUTH_SESSION_NAME, as);
			as.req = req;
			as.res = res;
			as.authProp = ap;
		}
		return as;
	}

	private static AuthSession createAuthSession(Class<?> authClass) throws AuthException {

		try {
			return (AuthSession) authClass.newInstance();

		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

}
