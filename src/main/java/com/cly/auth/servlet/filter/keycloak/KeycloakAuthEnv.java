package com.cly.auth.servlet.filter.keycloak;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cly.auth.base.AuthEnv;
import com.cly.comm.util.YamlParser;

public class KeycloakAuthEnv extends AuthEnv {

	private String realm;
	private String authServerUrl;
	private String resourceName;
	private String secret;
	private String resourceRedirectRootUrl;
	private String tokenUrl;
	private String authUrl;
	private String loginUrl;
	private String authorizeUrl;
	private String entitlementUrl;
	private String userInfoUrl;
	
	@Override
	public void init(YamlParser ypConf) {

		String authConf = "auth.config.";

		realm = ypConf.getStringValue(authConf + "realm");
	    authServerUrl = ypConf.getStringValue(authConf + "auth-server-url");
		resourceName = ypConf.getStringValue(authConf + "resource.name");
		resourceRedirectRootUrl = ypConf.getStringValue(authConf + "resource.redirectRootUrl");
		secret = ypConf.getStringValue(authConf + "credentials.secret");
		
		String urlRealm=this.authServerUrl + "/realms/" + this.realm;
		
		String sr =urlRealm+ "/protocol/openid-connect/";
		this.tokenUrl = sr + "token";
		this.authUrl = sr + "auth";
		this.loginUrl = this.authUrl + "?" + KeycloakConst.CLIENT_ID + "=" + this.resourceName
				+ "&response_type=code&scope=openid&redirect_uri=" + this.resourceRedirectRootUrl;
		this.userInfoUrl=sr+"userinfo";
				
		sr=urlRealm+"/authz/";
		this.authorizeUrl=sr+"authorize";
		this.entitlementUrl=sr+"entitlement";

		
		
	}

	public String getRealm() {
		return realm;
	}

	public String getAuthServerUrl() {
		return authServerUrl;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getResourceRedirectRootUrl() {
		return this.resourceRedirectRootUrl;
	}

	public String getSecret() {
		return secret;
	}

	public String getTokenUrl() {
		return this.tokenUrl;
	}

	public String getAuthUrl() {
		return this.authUrl;
	}
	
	public String getAuthorizeUrl() {
		return this.authorizeUrl;
	}
	
	public String getEntitlementUrl() {
		return this.entitlementUrl;
	}
	
	public String getUserInfoUrl() {
		return this.userInfoUrl;
	}

	@Override
	public void forwardLoginPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.sendRedirect(this.loginUrl + req.getRequestURI());

	}
	
	
}
