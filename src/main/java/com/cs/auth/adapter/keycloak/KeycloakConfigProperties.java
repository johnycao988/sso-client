package com.cs.auth.adapter.keycloak;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import com.cly.comm.client.http.HttpClient;
import com.cly.comm.client.http.HttpRequestParam;
import com.cs.auth.base.AuthException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class KeycloakConfigProperties {

	private final String authServerRootUrl;
	private final String authUrl;
	private final String tokenUrl;
	private final String clientRedirectRootUrl;
	private final String clientId;
	private final String clientSecret;
	private KeycloakToken permToken;

	public KeycloakConfigProperties(Properties p) throws AuthException {

		String[] PNS = { "KEYCLOAK.AUTH.SERVER.ROOT.URL", "KEYCLOAK.AUTH.URL",
				"KEYCLOAK.CLIENT.REDIRECT.ROOT.URL", "KEYCLOAK.CLIENT.ID", "KEYCLOAK.CLIENT.SECRET", "KEYCLOAK.TOKEN.URL" };

		String errInfo = "";

		String ns = " is not set.\r\n";

		authServerRootUrl = p.getProperty(PNS[0]);
		if (authServerRootUrl == null)
			errInfo = PNS[0] + ns;

		authUrl = p.getProperty(PNS[1]);
		if (authUrl == null)
			errInfo += PNS[1] + ns;

		clientRedirectRootUrl = p.getProperty(PNS[2]);
		if (clientRedirectRootUrl == null)
			errInfo += PNS[2] + ns;

		clientId = p.getProperty(PNS[3]);
		if (clientId == null)
			errInfo += PNS[3] + ns;

		clientSecret = p.getProperty(PNS[4]);
		if (clientSecret == null)
			errInfo += PNS[4] + ns;

		tokenUrl = p.getProperty(PNS[5]);
		if (tokenUrl == null)
			errInfo += PNS[5] + ns;
		
		if (errInfo.length() > 0)
			throw new AuthException(errInfo);

	}

	public String getAuthServerRootUrl() {
		return authServerRootUrl;
	}

	public String getAuthUrl() {
		return authUrl;
	}
	
	public String getTokeUrl() {
		return tokenUrl;
	}

	public String getClientRedirectRootUrl() {
		return clientRedirectRootUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public KeycloakToken getPermissionToken() throws  IOException {
		
		if (permToken == null || permToken.isExpired()) {

			return refreshPermissionToken();
		}

		getClaimsFromToken(permToken.getAccessToken());
		
		return permToken;
	}
	
	private KeycloakToken refreshPermissionToken() throws IOException{
		
		
		HttpRequestParam hp=new HttpRequestParam();
		hp.addHeader("Content-Type", "application/x-www-form-urlencoded");
		hp.addParam("grant_type", "client_credentials");
		hp.addParam("client_id", this.getClientId());
		hp.addParam("client_secret", this.getClientSecret()); 
		String url=this.getAuthServerRootUrl()+this.getTokeUrl();
		String sr=HttpClient.request(url,HttpClient.REQUEST_METHOD_POST,hp);
		
		System.out.println("results:"+sr);
		
		return new KeycloakToken(sr);
	}
	
 
	
	
	
	private Claims getClaimsFromToken(String token) {
	    Claims claims;
	    try {
	        claims = Jwts.parser()
	                .setSigningKey(this.getClientSecret())
	                .parseClaimsJws(token)
	                .getBody();
	    } catch (Exception e) {
	        claims = null;
	    }
	    
	    Iterator<Entry<String,Object>> it=claims.entrySet().iterator();
	    
	    while(it.hasNext()){
	    	
	    	Entry<String,Object> e=it.next();
	    	
	    	System.out.println(e.getKey()+":"+e.getValue());
	    }
	    
	    return claims;
	}



}
