package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;

/**
 * ユーザー情報画面Serviceインターフェース
 * 
 * @author kajiwara_takuya
 */
public interface SignupService {
	
	/**
	 * 画面の入力情報を元にユーザー情報テーブルの新規登録を行います。
	 * 
	 * @param form 入力情報
	 * @return 登録情報(ユーザー情報 Entity)、既に同じユーザーIDが登録されていた場合Empty
	 */
	public Optional<UserInfo> registerUserInfo(SignupForm form);
}
