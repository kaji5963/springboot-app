package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.constant.AuthorityKind;
import com.example.demo.constant.UserStatusKind;
import com.example.demo.dto.UserListInfo;
import com.example.demo.entity.UserInfo;
import com.example.demo.form.UserListForm;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.util.AppUtil;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー一覧画面Service実装クラス
 * 
 * @author kajiwara_takuya
 *
 */
@Service
@RequiredArgsConstructor
public class UserListServiceImpl implements UserListService {

	/** ユーザー情報テーブルDAO */
	private final UserInfoRepository repository;

	/** Dozer Mapper */
	private final Mapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserListInfo> editUserList() {
		return toUserListInfos(repository.findAll());
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UserListInfo> editUserListByParam(UserListForm form) {
		UserInfo userInfo = mapper.map(form, UserInfo.class);

		return toUserListInfos(findUserInfoByParam(userInfo));
	}

	/**
	 * ユーザー情報取得(条件付き)
	 * ユーザー情報を条件検索する
	 * 
	 * @param userInfo ユーザー情報（元データはform情報）
	 * @return 検索結果
	 */
	public List<UserInfo> findUserInfoByParam(UserInfo userInfo) {
		// %ログインID%を変数へ格納
		String loginIdParam = AppUtil.addWildCard(userInfo.getLoginId());

		// ユーザー状態種別
		UserStatusKind userStatusKind = userInfo.getUserStatusKind();

		// 利用権限
		AuthorityKind authorityKind = userInfo.getAuthorityKind();
		
		if (userStatusKind != null && authorityKind != null) {
	        return repository.findByLoginIdLikeAndUserStatusKindAndAuthorityKind(
	            loginIdParam, userStatusKind, authorityKind);
	    }
		
	    if (userStatusKind != null) {
	        return repository.findByLoginIdLikeAndUserStatusKind(loginIdParam, userStatusKind);
	    }
	    
	    if (authorityKind != null) {
	        return repository.findByLoginIdLikeAndAuthorityKind(loginIdParam, authorityKind);
	    }

	    return repository.findByLoginIdLike(loginIdParam);
	}

	/**
	 * ユーザー情報EntityのListをユーザー一覧情報DTOのListに変換します。
	 * 
	 * @param userInfos ユーザー情報EntityのList
	 * @return ユーザ一覧情報DTOのList
	 */
	private List<UserListInfo> toUserListInfos(List<UserInfo> userInfos) {
		ArrayList<UserListInfo> userListInfos = new ArrayList<UserListInfo>();

		for (UserInfo userInfo : userInfos) {
			UserListInfo userListInfo = mapper.map(userInfo, UserListInfo.class);

			userListInfo.setStatus(userInfo.getUserStatusKind().getDisplayValue());
			userListInfo.setAuthority(userInfo.getAuthorityKind().getDisplayValue());
			userListInfos.add(userListInfo);
		}

		return userListInfos;

	}

}