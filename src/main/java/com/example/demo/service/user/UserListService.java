package com.example.demo.service.user;

import java.util.List;

import com.example.demo.constant.UserDeleteResult;
import com.example.demo.dto.UserListInfo;
import com.example.demo.dto.UserSearchInfo;

/**
 *  ユーザー一覧画面Serviceインターフェース
 *  
 *  @author kajiwara_takuya
 */
public interface UserListService {
	
	/**
	 * ユーザー情報テーブルを全件検索し、ユーザー一覧画面に必要な情報へ変換して返却します。
	 * 
	 * @return ユーザー情報テーブルの全登録情報
	 */
	public List<UserListInfo> editUserList();
	
	/**
	 * ユーザー情報を条件検索した結果を表示用に変換して返却します。
	 * 
	 * @param searchDto 検索用DTO
	 * @return 検索結果
	 */
	public List<UserListInfo> editUserListByParam(UserSearchInfo dto);
	
	/**
	 * 指定されたIDのユーザー情報を削除します。
	 * 
	 * @param loginId ログインID
	 * @return 実行結果（エラー有無）
	 */
	public UserDeleteResult deleteUserInfoById(String loginId);
}
