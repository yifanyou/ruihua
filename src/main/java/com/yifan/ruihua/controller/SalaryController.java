package com.yifan.ruihua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SalaryController {
	@RequestMapping("/salary")
	public String show(){
		return "views/salary";
	}
}
