package com.example.demo.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.constant.UserDeleteResult;
import com.example.demo.constant.db.AuthorityKind;
import com.example.demo.constant.db.UserStatusKind;
import com.example.demo.dto.UserListInfo;
import com.example.demo.dto.UserSearchInfo;
import com.example.demo.entity.UserInfo;
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
	public List<UserListInfo> editUserListByParam(UserSearchInfo dto) {
		return toUserListInfos(findUserInfoByParam(dto));
	}
	
	public UserDeleteResult deleteUserInfoById(String loginId) {
		Optional<UserInfo> userInfo = repository.findById(loginId);
		
		if (userInfo.isEmpty()) {
			return UserDeleteResult.ERROR;
		}
		
		repository.deleteById(loginId);
		
		return UserDeleteResult.SUCCEED;
	}

	/**
	 * ユーザー情報の条件検索を行い、検索結果を返却します。
	 * 
	 * @param dto 検索情報DTO
	 * @return 検索結果
	 */
	public List<UserInfo> findUserInfoByParam(UserSearchInfo dto) {
		// %ログインID%を変数へ格納
		String loginIdParam = AppUtil.addWildCard(dto.getLoginId());

		// ユーザー状態種別
		UserStatusKind userStatusKind = dto.getUserStatusKind();

		// 利用権限
		AuthorityKind authorityKind = dto.getAuthorityKind();
		
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