package com.example.demo.service;

import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.AuthorityKind;
import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 *  ユーザー登録画面 Service実装クラス
 *  
 *  @author kajiwara_takuya
 */
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	/** ユーザー情報 Repository */
	private final UserInfoRepository repository;
	
	/** Dozer Mapper */
	private final Mapper mapper;
	
	/** PasswordEncoder */
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<UserInfo> registerUserInfo(SignupForm form) {
		Optional<UserInfo> userInfoExistedOpt = repository.findById(form.getLoginId());
		
		// DBに既にデータが存在していた場合に処理終了
		if (userInfoExistedOpt.isPresent()) {
			return Optional.empty();
		} 
		// SignupFormの各フィールドをUserInfoへ自動でマッピング（DozerによるBean変換）
		// passwordフィールドはUserInfoクラスにて除外
		UserInfo userInfo = mapper.map(form, UserInfo.class);
		
		// 入力されたパスワードをハッシュ化
		String endodedPassword = passwordEncoder.encode(form.getPassword());
		
		// ハッシュ化されたパスワードをuserInfoインスタンスへset
		userInfo.setPassword(endodedPassword);
		
		// 権限情報を格納(権限レベル低)
		userInfo.setAuthority(AuthorityKind.ITEM_WATCHER.getAuthorityKind());
		
		// saveメソッドで格納されたuserInfo情報をDBへ登録
		return  Optional.of(repository.save(userInfo));
	}
}
