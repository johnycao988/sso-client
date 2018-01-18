package com.cs.auth.adapter.keycloak;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cly.comm.client.http.HttpClient;
import com.cly.comm.client.http.HttpRequestParam;
import com.cly.comm.util.IDUtil;

import com.cs.auth.base.AuthException;
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

		if (code != null && sessionState != null) {
			codeToToken(code, sessionState);
		} else {

		}

		return null;

	}

	private String getClientBasicToken() {

		return IDUtil.Base64Encode(this.authProp.getClientId() + ":" + this.authProp.getClientSecret());
	}

	private Token codeToToken(String code, String sessionState) throws AuthException {

		try {

			HttpRequestParam hp = new HttpRequestParam();

			hp.addHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION,
					KeycloakConst.REQ_HEADER_VALUE_BAISC + getClientBasicToken());
			hp.addHeader(KeycloakConst.REQ_HEADER_CONTENT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_CONTENT_TYPE_FORM_URL_ENCODEED);
			hp.addParam(KeycloakConst.REQ_HEADER_GRANT_TYPE, KeycloakConst.REQ_HEADER_VALUE_AUTHORIZATION_CODE);
			hp.addParam(KeycloakConst.CLIENT_ID, this.authProp.getClientId());
			hp.addParam(KeycloakConst.CLIENT_SECRET, this.authProp.getClientSecret());
			hp.addParam(KeycloakConst.CODE, code);
			hp.addParam(KeycloakConst.REDIRECT_URL, this.authProp.getClientRedirectRootUrl() + req.getRequestURI());

			String reqUrl = this.authProp.getAuthServerRootUrl() + this.authProp.getTokenUri();

			String sr = HttpClient.request(reqUrl, HttpClient.REQUEST_METHOD_POST, hp);

			System.out.println(sr);

			return new KeycloakToken(sr);

		} catch (Exception e) {
			throw new AuthException(e);
		}
	}

	@Override
	public void forwardLoginPage() throws ServletException, IOException {

		StringBuffer sb=new StringBuffer();
		
		sb.append(authProp.getAuthServerRootUrl())
		.append(authProp.getAuthUri())
		.append("?")
		.append(KeycloakConst.CLIENT_ID)
		.append("=")
		.append(this.authProp.getClientId())
		.append("&")
		.append("response_type=code&scope=openid&redirect_uri=")
	    .append(this.authProp.getClientRedirectRootUrl())
	    .append(req.getRequestURI());

		System.out.println("****** req login url:" + sb.toString());

		res.sendRedirect(sb.toString());

	}

}
