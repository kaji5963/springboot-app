package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.LoginForm;

@Controller
public class LoginController {

	@GetMapping("login")
	public String view(Model model, LoginForm form) {
		return "login";
	}

	@PostMapping("login")
	public void login(LoginForm form) {
		System.out.println(form.toString());
	}
}
