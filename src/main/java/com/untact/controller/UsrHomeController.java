package com.untact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsrHomeController {
	
	@RequestMapping("/usr/home/main")
	@ResponseBody
	public String showMain() {
		System.out.println("showMain() 실행됨.");
		return "안녕";
	}
	

}
