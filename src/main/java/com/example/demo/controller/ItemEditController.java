package com.example.demo.controller;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.SessionKeyConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.constant.ViewNameConst;
import com.example.demo.entity.ItemInfo;
import com.example.demo.form.ItemEditForm;
import com.example.demo.service.item.ItemEditService;
import com.example.demo.util.AppUtil;
import com.github.dozermapper.core.Mapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 商品編集画面Controllerクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Controller
@RequiredArgsConstructor
public class ItemEditController {

	/** ユーザー編集画面Serviceクラス */
	private final ItemEditService service;

	/** セッションオブジェクト */
	private final HttpSession session;

	/** Dozer Mapper */
	private final Mapper mapper;

	/** メッセージソース */
	private final MessageSource messageSource;

	/** リダイレクトパラメータ：エラー有 */
//	private static final String REDIRECT_PRAM_ERR = "err";

	/**
	 * 前画面で選択された商品IDに紐づく商品情報を画面に表示します。
	 * 
	 * @param model モデル
	 * @return 表示画面
	 * @throws Exception 
	 */
	@GetMapping(UrlConst.ITEM_EDIT)
	public String view(Model model) {
		// セッションから選択された商品ID取得
		String itemId = (String) session.getAttribute(SessionKeyConst.SELECTED_ITEM_ID);

		//  商品IDから該当する情報を商品テーブルから取得
		Optional<ItemInfo> itemInfoOpt = service.searchItemInfo(itemId);

		// 商品IDが存在しなかった場合
		if (itemInfoOpt.isEmpty()) {
			model.addAttribute("message", AppUtil.getMessage(messageSource, MessageConst.NON_EXISTED_ITEM_ID));
			
			// TODO:リダイレクト先検討
			return ViewNameConst.MENU;
		}

		// 画面表示に必要な項目の設定
		model.addAttribute("itemEditForm", mapper.map(itemInfoOpt.get(), ItemEditForm.class));

		return ViewNameConst.ITEM_EDIT;
	}

	/**
	 * 画面の更新エラー時にエラーメッセージを表示します。
	 * 
	 * @param model モデル
	 * @return ユーザー編集エラー画面テンプレート名
	 */
	//	@GetMapping(value = UrlConst.USER_EDIT, params = REDIRECT_PRAM_ERR)
	//	public String viewWithError(Model model) {
	//		return ViewNameConst.USER_EDIT_ERROR;
	//	}

	/**
	 * 画面の入力情報をもとにユーザー情報を更新します。
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @param 認証ユーザー情報
	 * @param redirectAttributes リダイレクト用オブジェクト
	 * @return 表示画面
	 */
	//	@PostMapping(value = UrlConst.USER_EDIT, params = "update")
	//	public String updateUser(Model model, UserEditForm form, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
	//		// DTOクラスにフォームの情報をマッピング
	//		UserUpdateInfo updateDto = mapper.map(form, UserUpdateInfo.class);
	//		
	//		// セッションに格納されている選択されたログインIDをDTOへ格納
	//		updateDto.setLoginId((String) session.getAttribute(SessionKeyConst.SELECTED_LOGIN_ID));
	//		
	//		// 更新したユーザーIDを格納
	//		updateDto.setUpdateUserId(user.getUsername());
	//
	//		// データ更新後の情報を取得
	//		UserEditResult updateResult = service.updateUserInfo(updateDto);
	//		
	//		// 成功or失敗のメッセージ情報を取得
	//		UserEditMessage updateMessage = updateResult.getUpdateMessage();
	//		
	//		// 失敗した場合は、エラー画面へ遷移
	//		if (updateMessage == UserEditMessage.FAILED) {
	//			redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
	//					AppUtil.getMessage(messageSource, updateMessage.getMessageId()));
	//			redirectAttributes.addAttribute(REDIRECT_PRAM_ERR, "");
	//			return AppUtil.doRedirect(UrlConst.USER_EDIT);
	//		}
	//
	//		redirectAttributes.addFlashAttribute(ModelKey.IS_ERROR, false);
	//		redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
	//				AppUtil.getMessage(messageSource, updateMessage.getMessageId()));
	//
	//		return AppUtil.doRedirect(UrlConst.USER_EDIT);
	//	}
}