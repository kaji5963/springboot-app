package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.service.SignupService;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録画面 Controller
 * 
 * @author kajiwara_takuya
 */
@Controller
@RequiredArgsConstructor
public class SignupController {

	/** ユーザー登録画面 Service */
	private final SignupService service;
	
	/**
	 * 初期表示
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@GetMapping("/signup")
	public String view(Model model, SignupForm form) {
		return "signup";
	}

	/**
	 * ユーザー登録
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@PostMapping("/signup")
	public void signup(Model model, SignupForm form) {
		UserInfo userInfo = service.registerUserInfo(form);
	}
}
