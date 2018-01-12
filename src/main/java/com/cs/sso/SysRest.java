package com.cs.sso; 
 
 

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.oauth2.Auth; 
 

@Controller 
@RestController 
public class SysRest {  
 
	 
	@RequestMapping(value = {"/","/index","/welcome"},method = RequestMethod.GET)
	public String getVersion() {
		
		return "ss client - version v1.0";		
	}
	
 
	@RequestMapping("/token")
	public String getToken() throws ClientProtocolException, IOException { 
			
		return Auth.getToken();
		
	} 
	 

}
