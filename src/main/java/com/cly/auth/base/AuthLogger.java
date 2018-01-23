package com.cly.auth.base;
 
public class AuthLogger {
	
	public static void fatal(String errMsg){ 
		 
		System.err.println(errMsg);
		
	}

	public static void fatal(Exception e){
		e.printStackTrace();		
	}
	
	public static void error(String errMsg){ 
		 
		System.err.println(errMsg);
		
	}

	public static void error(Exception e){
		e.printStackTrace();		
	}
	
	public static void debug(Object msg){
		System.out.println(msg);		
	}

	public static void info(Object msg){
		System.out.println(msg);		
	}

	public static void warn(Object msg){
		System.out.println(msg);		
	}
}
