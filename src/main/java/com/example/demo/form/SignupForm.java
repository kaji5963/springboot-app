package com.example.demo.form;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * ユーザー登録画面 Formクラス
 * 
 * @author kajiwara_takuya
 */
@Data
public class SignupForm {

	/** ログインID */
	@Length(min = 4, max = 20)
	private String loginId;
	
	/** パスワード */
	@Length(min = 4, max = 20)
	private String password;
}
