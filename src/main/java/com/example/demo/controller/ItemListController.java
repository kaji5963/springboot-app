package com.example.demo.controller;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.ItemDeleteResult;
import com.example.demo.constant.ModelKey;
import com.example.demo.constant.SessionKeyConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.constant.ViewNameConst;
import com.example.demo.dto.ItemSearchInfo;
import com.example.demo.dto.StaffInfo;
import com.example.demo.entity.ItemInfo;
import com.example.demo.form.ItemListForm;
import com.example.demo.service.item.ItemListService;
import com.example.demo.util.AppUtil;
import com.github.dozermapper.core.Mapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 商品一覧画面Controllerクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Controller
@RequiredArgsConstructor
public class ItemListController {

	/** 商品一覧画面Serviceクラス */
	private final ItemListService service;

	/** セッションオブジェクト */
	private final HttpSession session;

	/** Dozer Mapper */
	private final Mapper mapper;

	/** メッセージソース */
	private final MessageSource messageSource;

	/** モデルキー：商品情報リスト */
	private static final String KEY_ITEMLIST_FORM = "itemListForm";

	/** モデルキー：商品情報リスト */
	private static final String KEY_ITEMLIST = "itemList";

	/** モデルキー：ユーザーIDリスト */
	private static final String KEY_USER_ID_OPTIONS = "userIdOptions";

	/**
	 * 画面の初期表示を行います。
	 * 
	 * <p>またその際、画面選択項目「アカウント状態」「所有権限」の選択肢を生成して画面に渡します。
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return ユーザー一覧画面テンプレート名
	 */
	@GetMapping(UrlConst.ITEM_LIST)
	public String view(Model model, ItemListForm form) {
		ItemSearchInfo searchDto = mapper.map(form, ItemSearchInfo.class);
		// 商品情報テーブルを条件検索した結果を取得
		List<ItemInfo> itemInfos = service.editItemListByParam(searchDto);

		// ユーザー情報テーブルを全件検索し、ユーザーIDとユーザー名の一覧を取得
		List<StaffInfo> userIdNames = service.obtainUserIdList();

		model.addAttribute(KEY_ITEMLIST, itemInfos);
		model.addAttribute(KEY_USER_ID_OPTIONS, userIdNames);

		return ViewNameConst.ITEM_LIST;
	}

	/**
	 * 検索条件に合致する商品情報を画面に表示します。
	 * 
	 * @param form 入力情報
	 * @param redirectAttributes リダイレクト用オブジェクト
	 * @return リダイレクトURL
	 */
	@PostMapping(value = UrlConst.ITEM_LIST, params = "search")
	public String searchItem(ItemListForm form, RedirectAttributes redirectAttributes) {
		ItemSearchInfo searchDto = mapper.map(form, ItemSearchInfo.class);

		// 商品情報テーブルを条件検索した結果を取得
		List<ItemInfo> itemInfos = service.editItemListByParam(searchDto);

		redirectAttributes.addFlashAttribute(KEY_ITEMLIST, itemInfos);
		redirectAttributes.addFlashAttribute(Conventions.getVariableName(form), form);

		return AppUtil.doRedirect(UrlConst.ITEM_LIST);
	}

	/**
	 * 選択行の商品情報をして、最新情報で画面を再表示します。
	 * 
	 * @param form 入力情報
	 * @return リダイレクトURL
	 */
	@PostMapping(value = UrlConst.ITEM_LIST, params = "edit")
	public String updateUser(ItemListForm form) {
		session.setAttribute(SessionKeyConst.SELECTED_ITEM_ID, form.getSelectedItemId());
		
		return AppUtil.doRedirect(UrlConst.ITEM_EDIT);
	}

	/**
	 * 選択行の商品情報を削除して、最新情報で画面を再表示します。
	 * 
	 * @param form 入力情報
	 * @param redirectAttributes リダイレクト用オブジェクト
	 * @return リダイレクトURL
	 */
	@PostMapping(value = UrlConst.ITEM_LIST, params = "delete")
	public String deleteItem(ItemListForm form, RedirectAttributes redirectAttributes) {
		// 選択された商品IDから商品を削除
		ItemDeleteResult result = service.deleteItemInfoById(form.getSelectedItemId());

		redirectAttributes.addFlashAttribute(ModelKey.IS_ERROR, result == ItemDeleteResult.ERROR);
		redirectAttributes.addFlashAttribute(ModelKey.MESSAGE,
				AppUtil.getMessage(messageSource, result.getMessageId()));

		// 削除後、フォーム情報の「選択された商品ID」は不要なためクリア
		redirectAttributes.addFlashAttribute(KEY_ITEMLIST_FORM, form.clearSelectedItemId());

		return AppUtil.doRedirect(UrlConst.ITEM_LIST);
	}
}