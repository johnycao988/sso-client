package com.cs.sso; 
 
 

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.auth.AuthMgr; 
 

@Controller 
@RestController 
public class SysRest {  
 
	 
	@RequestMapping(value = {"/","/index","/welcome","version","info"},method = RequestMethod.GET)
	public String getVersion() {
		
		return "SSO Client - version v1.0";		
	}
	
 
	@RequestMapping("/token")
	public String getToken() throws ClientProtocolException, IOException { 
			
		return "Token";
		
	} 
	 

}
