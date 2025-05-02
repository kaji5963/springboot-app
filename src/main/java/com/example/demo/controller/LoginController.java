package com.example.demo.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService service;

	@GetMapping("/login")
	public String view(Model model, LoginForm form) {
		return "login";
	}

	@PostMapping("/login")
	public String login(Model model, LoginForm form) {
		Optional<UserInfo> userInfo = service.SearchUserById(form.getLoginId());
		boolean isCorrectUserAuth = userInfo.isPresent() 
				&& form.getPassword().equals(userInfo.get().getPassword());
		
		if (isCorrectUserAuth) {
			return "redirect:/menu";
		} else {
			model.addAttribute("errorMsg", "ログインIDとパスワードの組み合わせが間違っています");
			return "login";
		}
	}
}
