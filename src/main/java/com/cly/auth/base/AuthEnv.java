package com.cly.auth.base;

import java.io.IOException;

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

	public abstract void forwardLoginPage(HttpServletRequest req, HttpServletResponse res) throws IOException;

	public AuthSecurityContext getAuthSecurityContent(HttpServletRequest req) {

		String authSecurityId = HttpUtil.getCookieValue(req, AUTH_SECURITY_ID);

		if (null == authSecurityId)
			return null;

		CacheService cacheSecurity = CacheManager.getCacheService(AuthConfig.AUTH_SECURITY_CONTENT_CACHE_NAME);

		AuthSecurityContext sc = (AuthSecurityContext) cacheSecurity.get(authSecurityId);

		if (null == sc) {

			CacheService cacheSession = CacheManager.getCacheService(AuthConfig.AUTH_SESSION_CACHE_NAME);
			String jsonToken = (String) cacheSession.get(authSecurityId);

			if (null != jsonToken) {

				AccessToken at = TokenUtil.parseJSONToken(jsonToken);

				if (null != at)
					sc = new AuthSecurityContext(authSecurityId, at);

			}
		}

		return sc;

	}

	public void setAuthSecurityContent(AuthSecurityContext securityContent, HttpServletResponse res)
			throws AuthException {

		if (null == securityContent)
			return;

		AccessToken accessToken = securityContent.getAccessToken();

		String jsonToken = accessToken.getJSONToken();

		String secuId = securityContent.getId();

		CacheService cs = CacheManager.getCacheService(AuthConfig.AUTH_SESSION_CACHE_NAME);

		cs.put(secuId, jsonToken);

		cs = CacheManager.getCacheService(AuthConfig.AUTH_SECURITY_CONTENT_CACHE_NAME);
		cs.put(secuId, securityContent);

		HttpUtil.setCookieValue(res, AUTH_SECURITY_ID, secuId, -1);

	}

}
