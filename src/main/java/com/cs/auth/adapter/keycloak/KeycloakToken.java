package com.cs.auth.adapter.keycloak; 
 
import com.cly.comm.util.JSONUtil; 
import com.cs.auth.base.Token;

import net.sf.json.JSONObject;
 

public class KeycloakToken extends Token {

	protected KeycloakToken(String jsonToken) {
		super(jsonToken); 
	
		JSONObject json=JSONObject.fromObject(jsonToken);
		
	    this.token=JSONUtil.getString(json, "access_token");
	    this.expireInSeconds=JSONUtil.getInt(json, 0, "expires_in");
	    this.refreshExpireInSeconds=JSONUtil.getInt(json, 0, "refresh_expires_in");
	    this.refreshToken=JSONUtil.getString(json, "refresh_token");
	    this.tokenId=JSONUtil.getString(json, "id_token");
	    this.sessionState=JSONUtil.getString(json, "session_state");
	    this.tokenType=JSONUtil.getString(json, "token_type");
	}
 
	
}
