package com.cs.auth.base;

public class AuthLogger {
	
	public static void error(String errMsg){
		
		System.err.println(errMsg);
		
	}

	public static void error(Exception e){
		e.printStackTrace();		
	}
	
	public static void debug(Object msg){
		System.out.println(msg);		
	}


}
