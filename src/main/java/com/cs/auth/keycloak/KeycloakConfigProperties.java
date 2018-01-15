package com.cs.auth.keycloak;

import java.util.Properties;

import com.cs.auth.AuthException;

public class KeycloakConfigProperties {

	private final String authServerRootUrl;
	private final String tokenAuthUrl;
	private final String clientRedirectRootUrl;
	private final String clientId;
	private final String clientSecret;

	public KeycloakConfigProperties(Properties p) throws AuthException {

		String[] PNS = { "KEYCLOAK.AUTH.SERVER.ROOT.URL", "KEYCLOAK.TOKEN.AUTH.URL",
				"KEYCLOAK.CLIENT.REDIRECT.ROOT.URL", "KEYCLOAK.CLIENT.ID", "KEYCLOAK.CLIENT.SECRET" };

		String errInfo = "";

		String ns = " is not set.\r\n";

		authServerRootUrl = p.getProperty(PNS[0]);
		if (authServerRootUrl == null)
			errInfo = PNS[0] + ns;

		tokenAuthUrl = p.getProperty(PNS[1]);
		if (tokenAuthUrl == null)
			errInfo += PNS[1] + ns;

		clientRedirectRootUrl = p.getProperty(PNS[2]);
		if (clientRedirectRootUrl == null)
			errInfo += PNS[2] + ns;

		clientId = p.getProperty(PNS[3]);
		if (clientId == null)
			errInfo += PNS[3] + ns;

		clientSecret = p.getProperty(PNS[4]);
		if (clientSecret == null)
			errInfo += PNS[4] + ns;

		if (errInfo.length()>0)
			throw new AuthException(errInfo);

	}

	public String getAuthServerRootUrl() {
		return authServerRootUrl;
	}

	public String getTokenAuthUrl() {
		return tokenAuthUrl;
	}

	public String getClientRedirectRootUrl() {
		return clientRedirectRootUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

}
