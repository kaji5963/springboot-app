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

import jakarta.servlet.http.HttpSession;
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

	/** セッションオブジェクト */
	private final HttpSession session;

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
			
			//TODO　おそらくここでバグっている
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

	/**
	 * 画面の入力情報からユーザー登録処理を呼び出します。
	 * 
	 * @param form 入力情報
	 * @param bdResult 入力情報の単項目チェック結果
	 * @param redirectAttributes リダイレクト用モデル
	 * @return 表示画面
	 */
	//	@PostMapping(UrlConst.SIGNUP)
	//	public String signup(@Validated SignupForm form, BindingResult bdResult, RedirectAttributes redirectAttributes) {
	//		// バリデーションチェック
	//		if (bdResult.hasErrors()) {
	//			// プロパティファイルからメッセージを取得
	//			editGuideMessage(form, bdResult, MessageConst.FORM_ERROR, redirectAttributes);
	//			return AppUtil.doRedirect(UrlConst.SIGNUP);
	//		}
	//		
	//		// form情報からユーザー情報を取得
	//		SignupResult signupResult = service.signup(mapper.map(form, SignupInfo.class));
	//		
	//		// 送信処理がエラーかどうか
	//		boolean isError = signupResult != SignupResult.SUCCEED;
	//		
	//		// エラーの場合の処理判定
	//		if (isError) {
	//			editGuideMessage(form, bdResult, signupResult.getMessageId(), redirectAttributes);
	//			return AppUtil.doRedirect(UrlConst.SIGNUP);
	//		}
	//		
	//		// ワンタイムパスワードで認証するログインIDをセッションへ格納
	//		session.setAttribute(SessionKeyConst.ONE_TIME_AUTH_LOGIN_ID, form.getLoginId());
	//		
	//		return AppUtil.doRedirect(UrlConst.SIGNUP_CONFIRM);
	//	}

	/**
	 * メッセージIDを使ってプロパティファイルからメッセージを取得し、画面に表示します。
	 * 
	 * <p>また、画面でメッセージを表示する際に通常メッセージとエラーメッセージとで色分けをするため、<br>
	 * その判定に必要な情報も画面に渡します。
	 * 
	 * @param form 入力情報
	 * @param bdResult 入力内容の単項目チェック結果
	 * @param messageId プロパティファイルから取得したいメッセージのID
	 * @param redirectAttributes リダイレクト用モデル
	 */
	//	private void editGuideMessage(SignupForm form, BindingResult bdResult, String messageId,
	//			RedirectAttributes redirectAttributes) {
	//		redirectAttributes.addFlashAttribute(ModelKey.MESSAGE, AppUtil.getMessage(messageSource, messageId));
	//		redirectAttributes.addFlashAttribute(ModelKey.IS_ERROR, true);
	//		redirectAttributes.addFlashAttribute(form);
	//		redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + FORM_CLASS_NAME, bdResult);
	//	}
}
