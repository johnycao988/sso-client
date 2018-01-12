package com.cs.sso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class AppPageRest {



	@RequestMapping(value = "/pages/{page}", method = RequestMethod.GET)
	public String pages(@PathVariable String page) {

	 		return page;
	}

	
	
	
}
