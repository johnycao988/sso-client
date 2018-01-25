package com.cly.auth.servlet.filter.keycloak;

import org.keycloak.common.util.Base64;

import com.cly.auth.base.AccessToken;
import com.cly.auth.base.AuthException;
import com.cly.comm.util.JSONUtil;

import net.sf.json.JSONObject;

public class KeycloakAccessToken extends AccessToken {

	protected KeycloakAccessToken(String responseJsonToken) throws AuthException {
		super(responseJsonToken);

		parse(responseJsonToken);

	}

	private void parse(String responseJsonToken) throws AuthException {

		try {

			JSONObject jo = JSONObject.fromObject(responseJsonToken);

			this.token = JSONUtil.getString(jo, "access_token");
			this.type = JSONUtil.getString(jo, "token_type");
			this.refreshToken = JSONUtil.getString(jo, "refresh_token");
			this.refreshExpireSeconds = JSONUtil.getInt(jo, "refresh_expires_in");
			this.expireSeconds = JSONUtil.getInt(jo, "expires_in"); 
			
			dumpToken(this.token);

		} catch (Exception e) {

			throw new AuthException(e);
		}

	}
	
	public static void dumpToken(String stoken) throws AuthException{
		
		String[] parts = stoken.split("\\.");
		if (parts.length < 2 || parts.length > 3)
			throw new AuthException("Parsing error");

		System.out.println("Header:" + new String(decode(parts[0])));
		System.out.println("Content:" + new String(decode(parts[1]))); 
	}

	private static byte[] decode(String s) {
		s = s.replace('-', '+'); // 62nd char of encoding
		s = s.replace('_', '/'); // 63rd char of encoding
		switch (s.length() % 4) // Pad with trailing '='s
		{
		case 0:
			break; // No pad chars in this case
		case 2:
			s += "==";
			break; // Two pad chars
		case 3:
			s += "=";
			break; // One pad char
		default:
			throw new RuntimeException("Illegal base64url string!");
		}
		try {
			// KEYCLOAK-2479 : Avoid to try gzip decoding as for some objects,
			// it may display exception to STDERR. And we know that object
			// wasn't encoded as GZIP
			return Base64.decode(s, Base64.DONT_GUNZIP);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
