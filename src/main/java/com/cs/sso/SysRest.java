package com.cs.sso;

import java.io.IOException;  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.auth.base.AuthException;
import com.cs.auth.base.AuthLogger;
import com.cs.auth.base.AuthMgr;
import com.cs.auth.base.AuthSession;

 

@Controller
@RestController
public class SysRest {

	@RequestMapping(value = { "/", "/index", "/welcome", "version", "info" }, method = RequestMethod.GET)
	public String getVersion() {

		return "SSO Client - version v1.0";
	}

 	@RequestMapping("/userAuthToken")
    public String getUserAuthToken(HttpServletRequest req, HttpServletResponse res) throws  IOException, AuthException {
 		
 		AuthSession as=AuthSession.getAuthSession(req, res, AuthMgr.getServletFilter(req, res));
 		
 		String st= as.getUserAuthencatedToken().getToken();
 		
 		AuthLogger.debug(st);
 		
 		return st;		
 		
 		
 	}
 	
	@RequestMapping("/clientPermToken")
    public String getClientPermToken(HttpServletRequest req, HttpServletResponse res) throws  IOException, AuthException {
 		
 		AuthSession as=AuthSession.getAuthSession(req, res, AuthMgr.getServletFilter(req, res));
 		
 	 	
	    String st= as.getClientPermissionToken().getToken();
 		
 		AuthLogger.debug(st);
 		
 		return st;	
 		
 		
 	}
 
 

}
