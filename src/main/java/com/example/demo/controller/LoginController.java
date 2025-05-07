package com.example.demo.controller;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.entity.UserInfo;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * ログイン画面 Controller
 * 
 * @author kajiwara_takuya
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

	/** ログイン画面 Service */
	private final LoginService service;

	/** PasswordEncoder */
	private final PasswordEncoder passwordEncoder;
	
	/** メッセージソース */
	private final MessageSource messageSource;
	
	/**
	 * 初期表示
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@GetMapping("/login")
	public String view(Model model, LoginForm form) {
		return "login";
	}

	/**
	 * ログイン
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@PostMapping("/login")
	public String login(Model model, LoginForm form) {
		// ログインIDでユーザー情報を検索
		Optional<UserInfo> userInfo = service.SearchUserById(form.getLoginId());
		
		// formから入力されたパスワード
		String formPassword = form.getPassword();
		
		// ハッシュ化されたDBのパスワードとformのパスワードを照合
		boolean isCorrectUserAuth = userInfo.isPresent() 
				&& passwordEncoder.matches(formPassword, userInfo.get().getPassword());
		
		if (isCorrectUserAuth) {
			return "redirect:/menu";
		} else {
			// キーでエラーメッセージを取得
			String errorMsg = AppUtil.getMessage(messageSource, MessageConst.LOGIN_WRONG_INPUT);
			
			model.addAttribute("errorMsg", errorMsg);
			return "login";
		}
	}
}
