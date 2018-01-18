package com.cs.auth.adapter.keycloak; 
 
import com.cs.auth.base.Token;
 

public class KeycloakToken implements Token {

	private String token; 

	private String refreshToken; 
	
	private String tokenType;

	private String tokenId;

	private String sessionState;
 

	public KeycloakToken(String jsonToken) {

	 

	} 
	 

	public String getTokenType() {
		return tokenType;
	}

	 

	public String getSessionState() {
		return sessionState;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getRefreshToken() {
		return this.refreshToken;
	}

	@Override
	public String getTokenId() {
		return tokenId;
	}

}
