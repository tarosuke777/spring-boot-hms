package com.tarosuke777.hms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("fromLoginDisplay", "1");
		return "login";
	}
}
