package com.cs.auth.base;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cly.comm.client.http.HttpUtil;

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

	public abstract Token getUserAuthencatedToken();

	public boolean needLogin() {

		Token userAuthToken = getUserAuthencatedToken();

		if (userAuthToken != null)
			return false;
		else
			return true;
	}

	public String getAuthSessionId() {
		return HttpUtil.getCookieValue(req, AUTH_SESSION_ID);
	}

	public void setAuthSessionId(String sessId) {

		HttpUtil.setCookieValue(res, AUTH_SESSION_ID, sessId, -1);

	}

	public static AuthSession getAuthSession(HttpServletRequest req, HttpServletResponse res,
			String authSessionClassName, AuthProperties ap) throws AuthException {

		AuthSession as = (AuthSession) req.getAttribute(AUTH_SESSION_NAME);

		if (as == null) {
			as = createAuthSession(authSessionClassName);
			req.setAttribute(AUTH_SESSION_NAME, as);
			as.req = req;
			as.res = res;
			as.authProp = ap;
		}
		return as;
	}

	private static AuthSession createAuthSession(String authSessionClassName) throws AuthException {

		try {
			return (AuthSession) Class.forName(authSessionClassName).newInstance();

		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

}
