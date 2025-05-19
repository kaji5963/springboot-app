
package com.example.demo.service;

import com.example.demo.dto.ItemRegisterResult;
import com.example.demo.form.ItemRegisterForm;

/**
 * 商品登録画面Serviceクラス
 * 
 * @author kajiwara_takuya
 *
 */
public interface ItemRegisterService {

	/**
	 * 商品を登録し登録結果を返却します。
	 * 
	 * @param form フォーム情報
	 * @param loginId ログインユーザーID
	 * @return 登録結果情報
	 */
	public ItemRegisterResult itemRegister(ItemRegisterForm form, String loginId);

}
