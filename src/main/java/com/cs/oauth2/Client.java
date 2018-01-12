package com.cs.oauth2;

public class Client {
	
	private String id;
	
	private String secret;
	
	public Client(String clientId, String clientSecret){
		
		this.id=clientId;
		this.secret=clientSecret;
	}
	
	public String getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	} 

}
