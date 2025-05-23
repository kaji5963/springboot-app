package com.example.demo.controller;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.ModelKey;
import com.example.demo.constant.SessionKeyConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.constant.UserDeleteResult;
import com.example.demo.constant.ViewNameConst;
import com.example.demo.constant.db.AuthorityKind;
import com.example.demo.constant.db.UserStatusKind;
import com.example.demo.dto.UserListInfo;
import com.example.demo.dto.UserSearchInfo;
import com.example.demo.form.UserListForm;
import com.example.demo.service.user.UserListService;
import com.example.demo.util.AppUtil;
import com.github.dozermapper.core.Mapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * ユーザー一覧画面Controllerクラス
 * 
 * @author kajiwara_takuya
 */
@Controller
@RequiredArgsConstructor
public class UserListController {
	
	/** セッション情報 */
	private final HttpSession session;
	
	/** ユーザー一覧画面Serviceクラス */
	private final UserListService service;
	
	/** Dozer Mapper */
	private final Mapper mapper;
	
	/** メッセージソース */
	private final MessageSource messageSource;
	
	/** モデルキー：ユーザー情報リストフォーム */
	private static final String KEY_USERLIST_FORM = "userListForm";
	
	/** モデルキー：ユーザー情報リスト */
	private static final String KEY_USERLIST = "userList";

	/** モデルキー：ユーザー情報リスト */
	private static final String KEY_USER_STATUS_KIND_OPTIONS = "userStatusKindOptions";
	
	/** モデルキー：ユーザー情報リスト */
	private static final String KEY_AUTHORITY_KIND_OPTIONS = "authorityKindOptions";
	
	/** モデルキー：操作種別 */
	private static final String KEY_OPERATION_KIND = "operationKind";
	
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
		// セッション情報を削除
		session.removeAttribute(SessionKeyConst.SELECTED_LOGIN_ID);
		
		model.addAttribute(KEY_USERLIST, editUserListInfo(model));
		model.addAttribute(KEY_USER_STATUS_KIND_OPTIONS, UserStatusKind.values());
		model.addAttribute(KEY_AUTHORITY_KIND_OPTIONS, AuthorityKind.values());

		return ViewNameConst.USER_LIST;
	}
	
	/**
	 * 初期表示、検索後や削除後のリダイレクトによる再表示のいずれかかを判定して画面に表示する一覧情報を作成します。
	 * 
	 * @param model モデル
	 * @return ユーザー一覧情報
	 */
	@SuppressWarnings("unchecked")
	private List<UserListInfo> editUserListInfo(Model model) {
		boolean doneSearchOrDelete = model.containsAttribute(KEY_OPERATION_KIND);
		
		if (doneSearchOrDelete) {
			OperationKind operationKind = (OperationKind) model.getAttribute(KEY_OPERATION_KIND);
			
			if (operationKind == OperationKind.SEARCH) {
				return (List<UserListInfo>) model.getAttribute(KEY_USERLIST);
			}
			
			if (operationKind == OperationKind.DELETE) {
				UserSearchInfo searchDto = mapper.map((UserListForm) model.getAttribute(KEY_USERLIST_FORM), UserSearchInfo.class);
				
				return service.editUserListByParam(searchDto);
			}
		}

		return service.editUserList();
	}
	
	/**
	 * ユーザー情報検索
	 * 
	 * @param model モデル
	 * @param form フォーム情報
	 * @param redirectAttributes リダイレクト用オブジェクト
	 * @return リダイレクトURL
	 */
	@PostMapping(value = UrlConst.USER_LIST, params = "search")
	public String searchUser(Model model, UserListForm form, RedirectAttributes redirectAttributes) {
		// フォーム入力値を検索用DTO（UserSearchInfo）にマッピング
		UserSearchInfo searchDto = mapper.map(form, UserSearchInfo.class);
		
		// 検索条件に合致したユーザー情報を取得
		List<UserListInfo> userInfos = service.editUserListByParam(searchDto);
		
		redirectAttributes.addFlashAttribute(KEY_USERLIST, userInfos);
		redirectAttributes.addFlashAttribute(KEY_USERLIST_FORM, form);
		redirectAttributes.addFlashAttribute(KEY_OPERATION_KIND, OperationKind.SEARCH);

		return AppUtil.doRedirect(UrlConst.USER_LIST);
	}
	
	/**
	 * 選択業の情報を編集して、最新情報で画面を再表示します。
	 * 
	 * @param form 入力情報
	 * @return リダイレクトURL
	 */
	@PostMapping(value = UrlConst.USER_LIST, params = "edit")
	public String updateUser(UserListForm form) {
		session.setAttribute(SessionKeyConst.SELECTED_LOGIN_ID, form.getSelectedLoginId());
		
		return AppUtil.doRedirect(UrlConst.USER_EDIT);
	}
	
	/**
	 * 選択業のユーザー情報を削除して、最新情報で画面を再表示します。
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @poram redirectAttributes リダイレクト用オブジェクト
	 * @return リダイレクトURL
	 */
	@PostMapping(value =UrlConst.USER_LIST, params = "delete")
	public String deleteUser(Model model, UserListForm form, RedirectAttributes redirectAttributes) {
		UserDeleteResult executeResult =service.deleteUserInfoById(form.getSelectedLoginId());
		
		redirectAttributes.addFlashAttribute(ModelKey.IS_ERROR, executeResult == UserDeleteResult.ERROR);
		redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
				AppUtil.getMessage(messageSource, executeResult.getMessageId()));
		// 削除後、フォーム情報の「選択されたログインID」は不要になるため、クリアします。
		redirectAttributes.addFlashAttribute(KEY_USERLIST_FORM, form.clearSelectedLoginId());
		redirectAttributes.addFlashAttribute(KEY_OPERATION_KIND, OperationKind.DELETE);

		return AppUtil.doRedirect(UrlConst.USER_LIST);
	}
	/**
	 * 操作種別Enum
	 * 
	 * @author kajiwara_takuya
	 */
	public enum OperationKind {
		SEARCH, DELETE;
	}
}


