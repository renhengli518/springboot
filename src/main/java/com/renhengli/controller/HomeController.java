package com.renhengli.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@RequestMapping("/")
	public String index(ModelMap map) {
		map.addAttribute("host", "welcone to index page !");
		return "index";
	}
}
