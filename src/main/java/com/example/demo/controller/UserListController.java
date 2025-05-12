package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.AuthorityKind;
import com.example.demo.constant.UrlConst;
import com.example.demo.constant.UserStatusKind;
import com.example.demo.dto.UserListInfo;
import com.example.demo.form.UserListForm;
import com.example.demo.service.UserListService;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー一覧画面Controllerクラス
 * 
 * @author kajiwara_takuya
 */
@Controller
@RequiredArgsConstructor
public class UserListController {
	
	/** ユーザー一覧画面Serviceクラス */
	private final UserListService service;
	
	/** モデルキー：ユーザー情報リスト */
	private static final String KEY_USERLIST = "userList";

	/** モデルキー：ユーザー情報リスト */
	private static final String KEY_USER_STATUS_KIND_OPTIONS = "userStatusKindOptions";
	
	/** モデルキー：ユーザー情報リスト */
	private static final String KEY_AUTHORITY_KIND_OPTIONS = "authorityKindOptions";
	
	/**
	 * 画面の初期表示を行います。
	 * 
	 * <p>またその際、画面選択項目「アカウント状態」「所有権限」の選択肢を生成して渡します。
	 * 
	 * @param model モデル
	 * @param form フォーム情報
	 * @return 表示画面
	 */
	@GetMapping(UrlConst.USER_LIST)
	public String view(Model model, UserListForm form) {
		List<UserListInfo> userInfos = service.editUserList();
		
		model.addAttribute(KEY_USERLIST, userInfos);
		model.addAttribute(KEY_USER_STATUS_KIND_OPTIONS, UserStatusKind.values());
		model.addAttribute(KEY_AUTHORITY_KIND_OPTIONS, AuthorityKind.values());

		return "userList";
	}
	
	/**
	 * ユーザー情報検索
	 * 
	 * @param model モデル
	 * @param form フォーム情報
	 * @return 表示画面
	 */
	@PostMapping(UrlConst.USER_LIST)
	public String searchUser(Model model, UserListForm form) {
		var userInfos = service.editUserListByParam(form);
		
		model.addAttribute(KEY_USERLIST, userInfos);
		model.addAttribute(KEY_USER_STATUS_KIND_OPTIONS, UserStatusKind.values());
		model.addAttribute(KEY_AUTHORITY_KIND_OPTIONS, AuthorityKind.values());

		return "userList";
	}
}
