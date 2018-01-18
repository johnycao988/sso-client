package com.cs.auth.base;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cly.cache.KVRedisService;
import com.cly.cache.KeyValue;
import com.cly.comm.client.http.HttpUtil;
import com.cly.comm.util.YamlParser;

public class AuthConfig {

	private HashMap<String, AuthServletFilter> hmAuthServletFilters;

	private final String AUTH_SERVER_ID = "AUTH.SERVER.ID"; 

	private KeyValue sessCache;

	public AuthConfig(String confFile) throws AuthException {

		hmAuthServletFilters = new HashMap<String, AuthServletFilter>();

		try (FileInputStream inStream = new FileInputStream(confFile)) {

			YamlParser ypr = new YamlParser();

			ypr.parse(inStream);

			String clientRedirectRootUrl = ypr.getStringValue("client.redirect.root.url");

			initSessionCache(ypr);

			YamlParser[] yps = ypr.getArray("auth");

			for (YamlParser yp : yps) {

				_AuthConfigImpl ac = new _AuthConfigImpl(yp, clientRedirectRootUrl);

				AuthServletFilter asf = this.initAuthServletFilter(ac);

				if (asf != null) {
					hmAuthServletFilters.put(ac.getAuthServerId(), asf);
				}

			}

		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	private void initSessionCache(YamlParser ypr) {

		String cacheType = ypr.getStringValue("session.cache.type");

		if (cacheType != null && cacheType.equalsIgnoreCase("redis")) {

			String redisServerUrl = ypr.getStringValue("session.cache.redis.server.url");
			int redisServerPort = ypr.getIntegerValue("session.cache.redis.server.port", -1);
			String redisServerAuthPwd = ypr.getStringValue("session.cache.redis.server.authPassword");

			KVRedisService kv = new KVRedisService();

			kv.init(redisServerUrl, redisServerPort, redisServerAuthPwd);

			this.sessCache = kv;

		}

	}

	private AuthServletFilter initAuthServletFilter(AuthProperties ap) {

		try {

			AuthServletFilter asf = (AuthServletFilter) Class.forName(ap.getAuthServletFilterClassName()).newInstance();

			asf.setAutProperties(ap);

			return asf;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public AuthServletFilter getAuthServletFilter(HttpServletRequest req, HttpServletResponse res) {

		String authServerId = HttpUtil.getCookieValue(req, AUTH_SERVER_ID);

		AuthServletFilter asf;

		if (authServerId == null) {

			authServerId = req.getParameter(AUTH_SERVER_ID);

			if (authServerId != null) {
				asf = hmAuthServletFilters.get(authServerId);
			} else {
				asf = this.getDefaultAuthServletFilter();
			}

			if (asf != null) {
				HttpUtil.setCookieValue(res, AUTH_SERVER_ID, asf.getAuthProperties().getAuthServerId(), -1);
			}

			return asf;

		} else {

			return hmAuthServletFilters.get(authServerId);
		}

	}

	private AuthServletFilter getDefaultAuthServletFilter() {

		Iterator<Entry<String, AuthServletFilter>> it = this.hmAuthServletFilters.entrySet().iterator();

		while (it.hasNext()) {

			AuthServletFilter asf = it.next().getValue();

			if (asf.getAuthProperties().isDefaultAuthServer()) {
				return asf;
			}
		}

		return null;
	}

	public AuthServletFilter[] getAllAuthServletFilters() {

		Iterator<Entry<String, AuthServletFilter>> it = this.hmAuthServletFilters.entrySet().iterator();

		ArrayList<AuthServletFilter> al = new ArrayList<AuthServletFilter>();

		while (it.hasNext()) {

			al.add(it.next().getValue());

		}

		return al.toArray(new AuthServletFilter[0]);
	}

}

class _AuthConfigImpl implements AuthProperties {

	private String authServerId;
	private String authServerName;
	private String authServerRootUrl;
	private String servletFilterClassName;
	private String authUri;
	private String tokenUri;
	private boolean isDefaultAuthServer;
	private YamlParser yp;
	private String clientId;
	private String clientSecret;
	private String clientRootRedirectUrl;

	public _AuthConfigImpl(YamlParser yp, String clientRootRedirectUrl) {

		this.clientRootRedirectUrl = clientRootRedirectUrl;
		this.yp = yp;
		this.authServerId = yp.getStringValue("server.id");
		this.authServerName = yp.getStringValue("server.name");
		this.authServerRootUrl = yp.getStringValue("server.root.url");
		this.authUri = yp.getStringValue("server.auth.uri");
		this.tokenUri = yp.getStringValue("server.token.uri");

		this.servletFilterClassName = yp.getStringValue("servlet.filter.class");
		this.clientId = yp.getStringValue("client.id");
		this.clientSecret = yp.getStringValue("client.secret");

		this.isDefaultAuthServer = yp.getBooleanValue("default", false);

	}

	@Override
	public String getClientId() {
		return this.clientId;
	}

	@Override
	public String getClientSecret() {
		return this.clientSecret;
	}

	@Override
	public String getClientRedirectRootUrl() {
		return this.clientRootRedirectUrl;
	}

	@Override
	public String getAuthServerId() {

		return this.authServerId;
	}

	@Override
	public String getAuthServerName() {

		return this.authServerName;
	}

	@Override
	public String getAuthServerRootUrl() {

		return this.authServerRootUrl;
	}

	@Override
	public String getAuthServletFilterClassName() {

		return this.servletFilterClassName;
	}

	@Override
	public String getAuthUri() {

		return this.authUri;
	}

	@Override
	public String getTokenUri() {

		return this.tokenUri;
	}

	@Override
	public boolean isDefaultAuthServer() {

		return this.isDefaultAuthServer;
	}

	@Override
	public String getAuthProperty(String name) {
		return yp.getStringValue(name);
	}

}
