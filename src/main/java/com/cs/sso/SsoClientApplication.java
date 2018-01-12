package com.cs.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan; 

@SpringBootApplication(scanBasePackages = "com.cs.sso") 
@ServletComponentScan 
public class SsoClientApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(SsoClientApplication.class, args); 
		
	}
	
 
}
