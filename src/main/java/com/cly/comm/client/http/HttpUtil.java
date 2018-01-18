package com.cly.comm.client.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpUtil {

	private HttpUtil() {

	}

	public static String getCookieValue(HttpServletRequest req, String cookieName) {

		Cookie[] css = req.getCookies();

		if (css != null)
			for (Cookie c : css) {
				String cn = c.getName();
				if (cn.equals(cookieName)) {
					return c.getValue();
				}
			}

		return null;

	}

	public static void setCookieValue(HttpServletResponse res, String cookieName, String cookieValue, int expiry) {

		Cookie cookie = new Cookie(cookieName, cookieValue);

		cookie.setMaxAge(expiry);

		res.addCookie(cookie);

	}

}
