package com.example.demo.form;

import lombok.Data;

/**
 * ユーザー登録画面 form
 * 
 * @author kajiwara_takuya
 */
@Data
public class SignupForm {

	/** ログインID */
	private String loginId;
	
	/** パスワード */
	private String password;
}
