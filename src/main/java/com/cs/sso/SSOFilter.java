package com.cs.sso;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.async.WebAsyncManager;

import com.cs.oauth2.Auth;

@Order(1)
@WebFilter(filterName = "testFilter1", urlPatterns = "/*")
public class SSOFilter implements Filter {

 
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("filter ...");
		
		this.printSessions(req, res);
				
		this.printHeaders(req, res);
		
		this.printCookies(req, res);
		
	 	 
		if(Auth.needLogin(req,res)) return;
		
		chain.doFilter(req, res);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
	
	private void printHeaders(ServletRequest request, ServletResponse response){
		
		HttpServletRequest req = (HttpServletRequest) request;
	    Enumeration<String> eh=req.getHeaderNames();
	    
	    
	    System.out.println("Headers:...........");
	    
	    while(eh.hasMoreElements()){
	    	String hn=eh.nextElement();
	    	System.out.println(hn+": "+req.getHeader(hn));
	    	
	    }
	    
	    eh=req.getAttributeNames();
    
	    System.out.println("Header Values:...........");
	    
	    while(eh.hasMoreElements()){
	    	String hn=eh.nextElement();
	    	System.out.println(hn+": "+ req.getAttribute(hn));
	    	
	    }
	    
	    WebAsyncManager wm=(WebAsyncManager)req.getAttribute("org.springframework.web.context.request.async.WebAsyncManager.WEB_ASYNC_MANAGER");
	    
	    System.out.println("wm:"+wm);
		
	}
	
    private void printCookies(ServletRequest request, ServletResponse response){
    	
    	HttpServletRequest req = (HttpServletRequest) request;
	    Cookie[] cs=req.getCookies();
	    
	    System.out.println("Cookies:...........");
	    
	    if(cs!=null)
	    for(Cookie c:cs){
	    	 
	    	System.out.println(c.getName()+": "+c.getValue());
	    	
	    }
		
	}
	
   private void printSessions(ServletRequest request, ServletResponse response){
	   
	   System.out.println("Session ...");
    	
    	HttpServletRequest req = (HttpServletRequest) request;
    	
    	HttpSession ses=req.getSession(false);
    	
        if(ses==null) return;
        
        @SuppressWarnings("deprecation")
		String[] vs=ses.getValueNames();
        
        System.out.println("Session Values....");
        
        for(String v:vs){
        	
        	 System.out.println(v+": "+ses.getValue(v));
        	
        }
        
        
        Enumeration<String> ets=ses.getAttributeNames();
        
        while(ets.hasMoreElements()){
        	
        	String n=ets.nextElement();
        	System.out.println(n+": "+ses.getAttribute(n));
        	
        }
	   
	   
	    	 
	    	 
	}
	
}
