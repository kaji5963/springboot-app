package com.example.demo.form;

import lombok.Data;

/**
 * ログイン画面 form
 * 
 * @author kajiwara_takuya
 */
@Data
public class LoginForm {

	/** ログインID */
	private String loginId;
	
	/** パスワード */
	private String password;
}
