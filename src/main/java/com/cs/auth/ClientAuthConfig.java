package com.cs.auth;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.cly.comm.util.YamlParser;

public class ClientAuthConfig {

	private String clientId;
	private String clientSecret;
	private String clientRootRedirectUrl;
	private HashMap<String, AuthServletFilter> hmAuthServletFilters;

	public ClientAuthConfig(String confFile) throws AuthException {

		hmAuthServletFilters = new HashMap<String, AuthServletFilter>();

		try (FileInputStream inStream = new FileInputStream(confFile)) {

			YamlParser ypr = new YamlParser();

			ypr.parse(inStream);

			clientId = ypr.getStringValue("client.id");
			clientSecret = ypr.getStringValue("client.secret");
			clientRootRedirectUrl = ypr.getStringValue("redirect.root.url");

			YamlParser[] yps = ypr.getArray("auth");

			for (YamlParser yp : yps) {

				_AuthConfigImpl ac = new _AuthConfigImpl(yp);

				AuthServletFilter asf = this.initAuthServletFilter(ac);

				if (asf != null) {
					hmAuthServletFilters.put(ac.getId(), asf);
				}

			}

		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	private AuthServletFilter initAuthServletFilter(AuthProperties ap) {

		try {

			AuthServletFilter asf = (AuthServletFilter) Class.forName(ap.getServletFilterClassName()).newInstance();

			asf.setAutProperties(ap);

			return asf;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getClientRootRedirectUrl() {
		return clientRootRedirectUrl;
	}

	public AuthServletFilter getAuthServletFilter(String authId) {

		return this.hmAuthServletFilters.get(authId);

	}

	public AuthServletFilter getDefaultAuthServletFilter() {

		Iterator<Entry<String, AuthServletFilter>> it = this.hmAuthServletFilters.entrySet().iterator();

		while (it.hasNext()) {

			AuthServletFilter asf = it.next().getValue();

			if (asf.getAuthProperties().isDefault()) {
				return asf;
			}
		}

		return null;
	}

}

class _AuthConfigImpl implements AuthProperties {

	private String id;
	private String name;
	private String serverRootUrl;
	private String servletFilterClassName;
	private String authUrl;
	private String tokenUrl;
	private boolean isDefault;
	private YamlParser yp;

	public _AuthConfigImpl(YamlParser yp) {
		this.yp = yp;
		this.id = yp.getStringValue("id");
		this.name = yp.getStringValue("name");
		this.serverRootUrl = yp.getStringValue("server.root.url");
		this.servletFilterClassName = yp.getStringValue("servlet.filter.class");
		this.authUrl = yp.getStringValue("auth.url");
		this.tokenUrl = yp.getStringValue("token.url");
		this.isDefault = yp.getBooleanValue("default", false);

	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getServerRootUrl() {
		return serverRootUrl;
	}

	@Override
	public String getServletFilterClassName() {
		return servletFilterClassName;
	}

	@Override
	public String getAuthUrl() {
		return authUrl;
	}

	@Override
	public String getTokenUrl() {
		return tokenUrl;
	}

	@Override
	public boolean isDefault() {

		return this.isDefault;
	}

	@Override
	public String getProperty(String name) {

		return yp.getStringValue(name);
	}

}
