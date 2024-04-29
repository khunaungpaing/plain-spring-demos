package com.khun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class HomeController {
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
    public String loginProcessing() {
        // No need to implement any logic here, Spring Security handles authentication
        // This method is just for handling the form submission
        return "redirect:/"; // Redirect to the home page after successful login
    }
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
}
