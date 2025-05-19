
package com.example.demo.dto;

import com.example.demo.constant.ItemRegisterMessage;
import com.example.demo.entity.ItemInfo;

import lombok.Data;

/**
 * 商品登録結果DTOクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Data
public class ItemRegisterResult {
	
	/** 商品情報 */
	private ItemInfo itemInfo;

	/** ユーザー更新結果メッセージEnum */
	private ItemRegisterMessage registerMessage;
}
