package com.cly.auth.base;

import java.io.FileInputStream;
import javax.servlet.FilterConfig;

import com.cly.cache.CacheManager;
import com.cly.cache.service.RedisService;
import com.cly.cache.service.SimpleService;
import com.cly.comm.util.YamlParser;

public class AuthConfig {

	private AuthServletFilter authServletFilter;

	public static final String AUTH_SESSION_CACHE_NAME = "AUTH_SESSION_CACHE";

	public static final String AUTH_SECURITY_CONTENT_CACHE_NAME = "AUTH_SECURITY_CONTENT_CACHE";

	public AuthConfig(String confFile, FilterConfig filterConfig) {

		try (FileInputStream inStream = new FileInputStream(confFile)) {

			YamlParser ypr = new YamlParser();

			ypr.parse(inStream);

			initSessionCache(ypr);

			String filterClass = ypr.getStringValue("auth.servlet.filter。class");

			AuthServletFilter asf = (AuthServletFilter) this.createInstance(filterClass);

			AuthEnv ap = (AuthEnv) this.createInstance(asf.getAuthEnvClass().getName());

			ap.init(ypr);

			asf.setAuthProperties(ap);
			asf.init(filterConfig);

		} catch (Exception e) {
			AuthLogger.error(e);
		}

	}

	private void initSessionCache(YamlParser ypr) {

		String cacheType = ypr.getStringValue("session.cache.type");

		if (cacheType != null && cacheType.equalsIgnoreCase("redis")) {

			String rd = "session.cache.server.redis.";

			String redisServerUrl = ypr.getStringValue(rd + "url");
			int redisServerPort = ypr.getIntegerValue(rd + "port", -1);
			String redisServerAuthPwd = ypr.getStringValue(rd + "authPassword");

			RedisService rs = new RedisService();

			rs.init(redisServerUrl, redisServerPort, redisServerAuthPwd);

			CacheManager.addCacheService(AUTH_SESSION_CACHE_NAME, rs);

		}

		CacheManager.addCacheService(AUTH_SECURITY_CONTENT_CACHE_NAME, new SimpleService());

	}

	private Object createInstance(String className) {

		try {

			Object ob = Class.forName(className).newInstance();

			return ob;

		} catch (Exception e) {
			AuthLogger.fatal(e);
		}

		return null;
	}

	public AuthServletFilter getAuthServletFilter() {

		return this.authServletFilter;

	}

}
