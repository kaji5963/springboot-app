package com.example.demo.controller;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.ItemRegisterMessage;
import com.example.demo.constant.ModelKey;
import com.example.demo.constant.UrlConst;
import com.example.demo.constant.ViewNameConst;
import com.example.demo.dto.ItemRegisterResult;
import com.example.demo.form.ItemRegisterForm;
import com.example.demo.service.ItemRegisterService;
import com.example.demo.util.AppUtil;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

/**
 * 商品登録画面 Controller
 * 
 * @author kajiwara_takuya
 */
@Controller
@RequiredArgsConstructor
public class ItemRegisterController {

	/** 商品登録画面 Service */
	private final ItemRegisterService service;

	/** メッセージソース */
	private final MessageSource messageSource;

	/** オブジェクト間項目輸送クラス */
	private final Mapper mapper;

	/** 画面で使用するフォームクラス名 */
	private final String FORM_CLASS_NAME = "itemRegisterForm";

	/** リダイレクトパラメータ：エラー有 */
	private static final String REDIRECT_PRAM_ERR = "err";

	/**
	 * 初期表示
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@GetMapping(UrlConst.ITEM_REGISTER)
	public String view(Model model) {
		// 初期表示なのか、リダイレクト後の表示なのかを判定(初期表示の場合true)
		boolean isInitialDisp = !model.containsAttribute(FORM_CLASS_NAME);

		// 初期表示処理の場合フォームの情報を格納
		if (isInitialDisp) {
			model.addAttribute(FORM_CLASS_NAME, new ItemRegisterForm());
		}
		
		return ViewNameConst.ITEM_REGISTER;
	}

	@PostMapping(UrlConst.ITEM_REGISTER)
	public String itemRegister(ItemRegisterForm form, RedirectAttributes redirectAttributes) {
		// 認証情報取得
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 認証情報からユーザーIDを取得
		String loginId = auth.getName();

		// 商品登録をし登録結果を取得する
		ItemRegisterResult result = service.itemRegister(form, loginId);

		// 成功or失敗のメッセージ情報を取得
		ItemRegisterMessage resultMessage = result.getRegisterMessage();

		// 登録結果が失敗の場合
		if (resultMessage == ItemRegisterMessage.FAILED) {
			redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
					AppUtil.getMessage(messageSource, resultMessage.getMessageId()));
			redirectAttributes.addAttribute(REDIRECT_PRAM_ERR, "");
			
			redirectAttributes.addFlashAttribute(FORM_CLASS_NAME, mapper.map(result.getItemInfo(), ItemRegisterForm.class));

			return AppUtil.doRedirect(UrlConst.ITEM_REGISTER);
		}

		// 登録処理が成功の場合、リダイレクト時に必要な情報を格納
		redirectAttributes.addFlashAttribute(ModelKey.IS_ERROR, false);
		redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
				AppUtil.getMessage(messageSource, resultMessage.getMessageId()));
		redirectAttributes.addFlashAttribute(FORM_CLASS_NAME, mapper.map(result.getItemInfo(), ItemRegisterForm.class));
		
		return AppUtil.doRedirect(UrlConst.ITEM_REGISTER);
	}
}
