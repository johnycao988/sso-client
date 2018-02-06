package com.cly.auth.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.cly.cache.CacheManager;
import com.cly.cache.CacheService;

public class AuthMgr {

	private static AuthConfig authConf;

	private AuthMgr() {

	}

	public static void init(FilterConfig filterConfig) throws ServletException {

		try {

			Iterator<Entry<Object, Object>> it = (Iterator<Entry<Object, Object>>) System.getProperties().entrySet();

			while (it.hasNext()) {

				Entry<Object, Object> e = it.next();
				System.out.println("Env Key:" + e.getKey() + " Value:" + e.getValue());
			}

			String confFile = System.getProperty("AUTH_CONFIG_FILE");

			if (confFile == null) {
				confFile = "/config/auth.config.yml";
			}

			authConf = new AuthConfig(confFile, filterConfig);

		} catch (Exception e) {
			AuthLogger.fatal(e);
		}

	}

	public static AuthServletFilter getAuthServletFilter() {
		return authConf.getAuthServletFilter();
	}

	public static void destroy() {

		if (authConf != null && authConf.getAuthServletFilter() != null) {

			authConf.getAuthServletFilter().destroy();

		}

	}

}