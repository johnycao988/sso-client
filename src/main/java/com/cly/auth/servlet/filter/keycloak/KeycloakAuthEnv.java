package com.cly.auth.servlet.filter.keycloak; 
import com.cly.auth.base.AuthEnv;
import com.cly.comm.util.YamlParser;

public class KeycloakAuthEnv extends AuthEnv {

    private String realm;
    private String authServerUrl;
    private String resourceName;
    private String secret;

    
	@Override
	public void init(YamlParser ypConf){ 
		
		String authConf="auth.config.";
		
		realm=ypConf.getStringValue(authConf+"realm");
		authServerUrl=ypConf.getStringValue(authConf+"auth-server-url");
	    resourceName=ypConf.getStringValue(authConf+"resource");
		secret=ypConf.getStringValue(authConf+"credentials.secret"); 

	}

	public String getRealm() {
		return realm;
	}

 
	public String getAuthServerUrl() {
		return authServerUrl;
	}

 
	public String getResourceName() {
		return resourceName;
	} 
 
	public String getSecret() {
		return secret;
	}
 
	
	 
}
