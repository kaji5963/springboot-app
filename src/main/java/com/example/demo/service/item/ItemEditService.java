
package com.example.demo.service.item;

import java.util.Optional;

import com.example.demo.entity.ItemInfo;

/**
 * 商品編集画面Serviceクラス
 * 
 * @author kajiwara_takuya
 *
 */
public interface ItemEditService {

	/**
	 * 商品IDを使って商品情報テーブルを検索し、検索結果を返却します。
	 * 
	 * @param itemId 商品ID
	 * @return 該当の商品情報テーブル登録情報
	 */
	public Optional<ItemInfo> searchItemInfo(String itemId);

	/**
	 * ユーザー情報テーブルを更新します。
	 * 
	 * @param userUpdateInfo ユーザー更新情報
	 * @return 更新結果
	 */
//	public UserEditResult updateUserInfo(UserUpdateInfo userUpdateInfo);

}
