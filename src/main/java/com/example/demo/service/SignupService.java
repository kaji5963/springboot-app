package com.example.demo.service;

import org.dozer.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 *  ユーザー登録画面 Service
 *  
 *  @author kajiwara_takuya
 */
@Service
@RequiredArgsConstructor
public class SignupService {

	/** ユーザー情報 Repository */
	private final UserInfoRepository repository;
	
	/** Dozer Mapper */
	private final Mapper mapper;
	
	/** PasswordEncoder */
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * ユーザー情報テーブル 新規登録
	 * 
	 * @param form 入力情報
	 * @return 登録情報(ユーザー情報 Entity)
	 */
	public UserInfo registerUserInfo(SignupForm form) {
		// SignupFormの各フィールドをUserInfoへ自動でマッピング（DozerによるBean変換）
		// passwordフィールドはUserInfoクラスにて除外
		UserInfo userInfo = mapper.map(form, UserInfo.class);
		
		// 入力されたパスワードをハッシュ化
		String endodedPassword = passwordEncoder.encode(form.getPassword());
		
		// ハッシュ化されたパスワードをuserInfoインスタンスへset
		userInfo.setPassword(endodedPassword);
		
		// saveメソッドで格納されたuserInfo情報をDBへ登録
		return  repository.save(userInfo);
	}
}
