package com.cs.auth.base;

public class AuthMgr {

	private static ClientAuthConfig authConf = init();

	private AuthMgr() {

	}

	private static ClientAuthConfig init() {

		try {
			String confFile = System.getProperty("AUTH_CONFIG_FILE");

			if (confFile == null) {
				confFile = "/config/auth.config.yml";
			}

			return new ClientAuthConfig(confFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static AuthServletFilter getServletFilter(String authId) {
		return authConf.getAuthServletFilter(authId);
	}

	public static AuthServletFilter getDefaultAuthServletFilter() {
		return authConf.getDefaultAuthServletFilter();
	}

	public String getClientId() {
		return authConf.getClientId();
	}

	public String getClientSecret() {
		return authConf.getClientSecret();
	}

	public String getClientRootRedirectUrl() {
		return authConf.getClientRootRedirectUrl();
	}

}