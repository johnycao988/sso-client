package com.cs.sso;

import java.io.IOException;  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cly.auth.base.AuthException;
import com.cly.auth.base.AuthLogger;
import com.cly.auth.base.AuthMgr; 

 

@Controller
@RestController
public class SysRest {

	@RequestMapping(value = { "/", "/index", "/welcome", "version", "info" }, method = RequestMethod.GET)
	public String getVersion() {

		return "SSO Client - version v1.0";
	}

 	
    @RequestMapping("/func/{scope}/{funcName}")  
    public String pathVariable(@PathVariable("scope")String scope, @PathVariable("funcName")String funcName){  
       return "scope:"+scope+" function name:"+funcName;   
    } 
 
 

}
