package com.example.demo.controller;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.ModelKey;
import com.example.demo.constant.SessionKeyConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.constant.UserEditMessage;
import com.example.demo.constant.ViewNameConst;
import com.example.demo.constant.db.AuthorityKind;
import com.example.demo.constant.db.UserStatusKind;
import com.example.demo.dto.UserEditInfo;
import com.example.demo.dto.UserEditResult;
import com.example.demo.dto.UserUpdateInfo;
import com.example.demo.entity.UserInfo;
import com.example.demo.form.UserEditForm;
import com.example.demo.service.user.UserEditService;
import com.example.demo.util.AppUtil;
import com.github.dozermapper.core.Mapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * ユーザー編集画面Controllerクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Controller
@RequiredArgsConstructor
public class UserEditController {

	/** ユーザー編集画面Serviceクラス */
	private final UserEditService service;

	/** セッションオブジェクト */
	private final HttpSession session;

	/** Dozer Mapper */
	private final Mapper mapper;

	/** メッセージソース */
	private final MessageSource messageSource;
	
	/** リダイレクトパラメータ：エラー有 */
	private static final String REDIRECT_PRAM_ERR = "err";

	/**
	 * 前画面で選択されたログインIDに紐づくユーザー情報を画面に表示します。
	 * 
	 * @param model モデル
	 * @return 表示画面
	 * @throws Exception 
	 */
	@GetMapping(UrlConst.USER_EDIT)
	public String view(Model model, UserEditForm form) {
		// セッションから選択されたユーザーのログインID取得
		String loginId = (String) session.getAttribute(SessionKeyConst.SELECTED_LOGIN_ID);
		
		// ログインIDから該当するデータを取得
		Optional<UserInfo> userInfoOpt = service.searchUserInfo(loginId);
		
		// データの存在チェック
		if (userInfoOpt.isEmpty()) {
			model.addAttribute("message", AppUtil.getMessage(messageSource, MessageConst.USEREDIT_NON_EXISTED_LOGIN_ID));
			return ViewNameConst.USER_EDIT_ERROR;
		}
		
		// 画面表示に必要な共通項目の設定
		UserInfo userInfo = userInfoOpt.get();
		model.addAttribute("userEditForm", mapper.map(userInfo, UserEditForm.class));
		model.addAttribute("userEditInfo", mapper.map(userInfo, UserEditInfo.class));
		model.addAttribute("userStatusKindOptions", UserStatusKind.values());
		model.addAttribute("authorityKindOptions", AuthorityKind.values());

		return ViewNameConst.USER_EDIT;
	}
	
	/**
	 * 画面の更新エラー時にエラーメッセージを表示します。
	 * 
	 * @param model モデル
	 * @return ユーザー編集エラー画面テンプレート名
	 */
	@GetMapping(value = UrlConst.USER_EDIT, params = REDIRECT_PRAM_ERR)
	public String viewWithError(Model model) {
		return ViewNameConst.USER_EDIT_ERROR;
	}

	/**
	 * 画面の入力情報をもとにユーザー情報を更新します。
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @param 認証ユーザー情報
	 * @param redirectAttributes リダイレクト用オブジェクト
	 * @return 表示画面
	 */
	@PostMapping(value = UrlConst.USER_EDIT, params = "update")
	public String updateUser(Model model, UserEditForm form, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
		// DTOクラスにフォームの情報をマッピング
		UserUpdateInfo updateDto = mapper.map(form, UserUpdateInfo.class);
		
		// セッションに格納されている選択されたログインIDをDTOへ格納
		updateDto.setLoginId((String) session.getAttribute(SessionKeyConst.SELECTED_LOGIN_ID));
		
		// 更新したユーザーIDを格納
		updateDto.setUpdateUserId(user.getUsername());

		// データ更新後の情報を取得
		UserEditResult updateResult = service.updateUserInfo(updateDto);
		
		// 成功or失敗のメッセージ情報を取得
		UserEditMessage updateMessage = updateResult.getUpdateMessage();
		
		// 失敗した場合は、エラー画面へ遷移
		if (updateMessage == UserEditMessage.FAILED) {
			redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
					AppUtil.getMessage(messageSource, updateMessage.getMessageId()));
			redirectAttributes.addAttribute(REDIRECT_PRAM_ERR, "");
			return AppUtil.doRedirect(UrlConst.USER_EDIT);
		}

		redirectAttributes.addFlashAttribute(ModelKey.IS_ERROR, false);
		redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
				AppUtil.getMessage(messageSource, updateMessage.getMessageId()));

		return AppUtil.doRedirect(UrlConst.USER_EDIT);
	}
}