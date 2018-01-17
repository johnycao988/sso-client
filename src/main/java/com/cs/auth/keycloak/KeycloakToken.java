package com.cs.auth.keycloak;

import java.util.Calendar;

import com.cly.comm.util.JSONUtil;

import net.sf.json.JSONObject;

public class KeycloakToken {

	private String accessToken;

	private int inExpiredSeonds;

	private String tokenType;

	private String tokeId;

	private String sessionState;

	private long refreshTime;

	public KeycloakToken(String jsonMsg) {

		JSONObject jo = JSONObject.fromObject(jsonMsg);

		this.accessToken = JSONUtil.getString(jo, "access_token");
		this.tokenType = JSONUtil.getString(jo, "token_type");
		this.tokeId = JSONUtil.getString(jo, "id_token");
		this.sessionState = JSONUtil.getString(jo, "session_state");
		this.inExpiredSeonds = JSONUtil.getInt(jo, 0, "expires_in");

		refreshTime = Calendar.getInstance().getTimeInMillis();

	}

	public String getAccessToken() {
		return accessToken;
	}

	public boolean isExpired() {

		long ct = (Calendar.getInstance().getTimeInMillis() - refreshTime) / 1000;

		if (ct < 0 || ct >= inExpiredSeonds)
			return true;
		
		else
			return false;
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getTokeId() {
		return tokeId;
	}

	public String getSessionState() {
		return sessionState;
	}

}
