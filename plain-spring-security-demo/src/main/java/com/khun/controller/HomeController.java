package com.khun.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class HomeController {
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	
	@GetMapping("/")
	public String home(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username",username);
		return "home";
	}
}
