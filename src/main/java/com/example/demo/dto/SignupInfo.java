package com.example.demo.dto;

import lombok.Data;

/**
 * ユーザー編集画面DTOクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Data
public class SignupInfo {

	/** ログインID */
	private String loginId;

	/** パスワード */
	private String password;

	/** メールアドレス */
	private String mailAddress;
}