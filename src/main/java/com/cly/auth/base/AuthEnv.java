package com.cly.auth.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cly.cache.CacheManager;
import com.cly.cache.CacheService;
import com.cly.comm.client.http.HttpUtil;
import com.cly.comm.util.YamlParser;

public abstract class AuthEnv {

	private final static String AUTH_SECURITY_ID = "AUTH_SECURITY_ID";

	public AuthEnv() {

	}

	public abstract void init(YamlParser ypConf);

	public String getSecurityContentId(HttpServletRequest req) {
		return HttpUtil.getCookieValue(req, AUTH_SECURITY_ID);
	}

	public SecurityContent getSecurityContent(String authSecurityId) {

		if (null == authSecurityId)
			return null;

		CacheService cacheSecurity = CacheManager.getCacheService(AuthConfig.AUTH_SECURITY_CONTENT_CACHE_NAME);

		return (SecurityContent) cacheSecurity.get(authSecurityId);

	}

	public String getAccssTokenFromSessionCache(String authSecurityId) {

		if (null == authSecurityId)
			return null;

		CacheService cacheSession = CacheManager.getCacheService(AuthConfig.AUTH_SESSION_CACHE_NAME);

		return (String) cacheSession.get(authSecurityId);

	}

	public void setSecurityContent(SecurityContent securityContent, HttpServletResponse res) {

		if (null == securityContent)
			return;

		String token = securityContent.getAccessToken().getToken();
		String secuId = securityContent.getId();

		CacheService cs = CacheManager.getCacheService(AuthConfig.AUTH_SESSION_CACHE_NAME);
		cs.put(secuId, token);

		cs = CacheManager.getCacheService(AuthConfig.AUTH_SECURITY_CONTENT_CACHE_NAME);
		cs.put(secuId, securityContent);

		HttpUtil.setCookieValue(res, AUTH_SECURITY_ID, secuId, -1);

	}

}
