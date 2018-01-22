package com.cs.auth.adapter.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cly.comm.client.http.HttpClient;
import com.cly.comm.client.http.HttpRequestParam;
import com.cly.comm.util.IDUtil;

import com.cs.auth.base.AuthException;
import com.cs.auth.base.AuthLogger;
import com.cs.auth.base.AuthSession;
import com.cs.auth.base.Token;

public class KeycloakSession extends AuthSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Token getUserAuthencatedToken() throws AuthException {

		String code = req.getParameter(KeycloakConst.CODE);

		String sessionState = req.getParameter(KeycloakConst.SESSION_STATE);

		Token uaToken = null;

		if (code != null && sessionState != null) {

			uaToken = codeToUserAuthentcatedToken(code, sessionState);

			if (uaToken != null) {
				String sid = this.createAuthSessionId();
				this.authProp.getKeyValueService().set(sid, uaToken.getJSONToken(), uaToken.refreshExpireInSeconds());
				this.setAuthSessionId(sid);
			}

		} else {

			String sid = this.getAuthSessionId();

			String jsonUAToken = null;

			if (sid != null)
				jsonUAToken = this.authProp.getKeyValueService().get(sid);

			if (jsonUAToken != null) {

				uaToken = new KeycloakToken(jsonUAToken);

			}

		}

		return uaToken;

	}

	private String getClientBasicToken() {

		return IDUtil.Base64Encode(this.authProp.getClientId() + ":" + this.authProp.getClientSecret());
	}

	private Token codeToUserAuthentcatedToken(String code, String sessionState) throws AuthException {

		try {

			HttpRequestParam hp = new HttpRequestParam();

			hp.addHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION,
					KeycloakConst.REQ_HEADER_VALUE_BAISC + getClientBasicToken());
			hp.addHeader(KeycloakConst.REQ_HEADER_CONTENT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_CONTENT_TYPE_FORM_URL_ENCODEED);
			hp.addParam(KeycloakConst.REQ_HEADER_GRANT_TYPE, KeycloakConst.REQ_HEADER_VALUE_AUTHORIZATION_CODE);
			hp.addParam(KeycloakConst.CODE, code);
			hp.addParam(KeycloakConst.REDIRECT_URL, this.authProp.getClientRedirectRootUrl() + req.getRequestURI());

			String reqUrl = this.authProp.getAuthServerRootUrl() + this.authProp.getTokenUri(); 

			String sr = HttpClient.request(reqUrl, HttpClient.REQUEST_METHOD_POST, hp);
			
			AuthLogger.debug("Get User Authenicated Token:\r\n"+sr);

			return new KeycloakToken(sr);

		} catch (Exception e) {
			throw new AuthException(e);
		}
	}

	@Override
	public void forwardLoginPage() throws AuthException {

		try {
			StringBuffer sb = new StringBuffer();

			sb.append(authProp.getAuthServerRootUrl()).append(authProp.getAuthUri()).append("?")
					.append(KeycloakConst.CLIENT_ID).append("=").append(this.authProp.getClientId()).append("&")
					.append("response_type=code&scope=openid&redirect_uri=")
					.append(this.authProp.getClientRedirectRootUrl()).append(req.getRequestURI());

			res.sendRedirect(sb.toString());
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	@Override
	public Token getClientPermissionToken() throws AuthException {

		try {

			Token clientPermToken = this.authProp.getClientPermissionToken();

			if (clientPermToken != null) {
				if (!clientPermToken.isExpired())
					return clientPermToken;
				else if (!clientPermToken.isRefreshExpired()) {
					clientPermToken = this.refreshToken(clientPermToken);
					return clientPermToken;
				}
			}

			HttpRequestParam hp = new HttpRequestParam();

	 		hp.addHeader(KeycloakConst.REQ_HEADER_CONTENT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_CONTENT_TYPE_FORM_URL_ENCODEED);
			
			hp.addParam(KeycloakConst.REQ_HEADER_GRANT_TYPE, KeycloakConst.REQ_HEADER_VALUE_CLIENT_CREDENTIALS);
			hp.addParam(KeycloakConst.CLIENT_ID, this.authProp.getClientId());
			hp.addParam(KeycloakConst.CLIENT_SECRET, this.authProp.getClientSecret());

			String reqUrl = this.authProp.getAuthServerRootUrl() + this.authProp.getTokenUri();
 

			String sr = HttpClient.request(reqUrl, HttpClient.REQUEST_METHOD_POST, hp);

			AuthLogger.debug("Get client permission Token:\r\n"+sr);
			
			return new KeycloakToken(sr);

		} catch (Exception e) {
			throw new AuthException(e);
		}
	}

	@Override
	public Token refreshToken(Token token) throws AuthException {
		// TODO Auto-generated method stub
		return null;
	}

}
