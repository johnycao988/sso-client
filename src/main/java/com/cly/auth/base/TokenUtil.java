package com.cly.auth.base;

import com.cly.comm.util.JSONUtil;

import net.sf.json.JSONObject;

public class TokenUtil {

	public final static String TOKEN = "token";
	public final static String TOKEN_REFRESH = "refreshToken";
	public final static String TOKEN_TYPE = "type";
	public final static String TOKEN_EXPIRE_SECONDS = "expireSeconds";
	public final static String TOKEN_REFRESH_EXPIRE_SECONDS = "refreshExpireSeconds";

	public static String toJSONToken(AccessToken accessToken) {

		try {
			JSONObject jo = new JSONObject();
			jo.put(TOKEN, accessToken.getToken());
			jo.put(TOKEN_REFRESH, accessToken.getRefreshToken());
			jo.put(TOKEN_TYPE, accessToken.getType());
			jo.put(TOKEN_EXPIRE_SECONDS, accessToken.getExpireSeconds());
			jo.put(TOKEN_REFRESH_EXPIRE_SECONDS, accessToken.getRefreshExpireSeconds());
			return jo.toString();
		} catch (Exception e) {
			return null;
		}

	}

	public static AccessToken parseJSONToken(String jsonToken) {

		try {

			class BaseAccessToken extends AccessToken {

				protected BaseAccessToken(String responseJsonToken) throws AuthException {
					super(responseJsonToken);

					JSONObject jo = JSONObject.fromObject(responseJsonToken);
					this.token = JSONUtil.getString(jo, TOKEN);
					this.refreshToken = JSONUtil.getString(jo, TOKEN_REFRESH);
					this.type = JSONUtil.getString(jo, TOKEN_TYPE);
					this.expireSeconds = JSONUtil.getInt(jo, TOKEN_EXPIRE_SECONDS);
					this.refreshExpireSeconds = JSONUtil.getInt(jo, TOKEN_REFRESH_EXPIRE_SECONDS);

				}
			}

			return new BaseAccessToken(jsonToken);

		} catch (Exception e) {
			return null;
		}

	}

}
