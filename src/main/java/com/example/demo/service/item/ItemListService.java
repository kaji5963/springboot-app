package com.example.demo.service.item;

import java.util.List;

import com.example.demo.constant.ItemDeleteResult;
import com.example.demo.dto.ItemSearchInfo;
import com.example.demo.dto.StaffInfo;
import com.example.demo.entity.ItemInfo;

/**
 * 商品一覧画面Serviceインターフェース
 * 
 * @author kajiwara_takuya
 *
 */
public interface ItemListService {

	/**
	 * ユーザー情報テーブルを全件検索し、ユーザーIDとユーザー名の一覧を返却します。
	 * 
	 * @return ユーザー情報テーブルに登録されているユーザーIDとユーザー名のリスト
	 */
	public List<StaffInfo> obtainUserIdList();

	/**
	 * 商品情報テーブルを条件検索した結果を返却します。
	 * 
	 * @param dto 検索に使用するパラメーター
	 * @return 検索結果
	 */
	public List<ItemInfo> editItemListByParam(ItemSearchInfo dto);
	
	/**
	 * 商品テーブルから指定された商品IDを削除します
	 * 
	 * @param itemId
	 * @return 削除結果
	 */
	public ItemDeleteResult deleteItemInfoById(String itemId);
}