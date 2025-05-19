package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品登録結果メッセージEnumクラス
 * 
 * @author kajiwara_takuya
 */
@Getter
@AllArgsConstructor
public enum ItemRegisterMessage {

	/* 登録成功 **/
	SUCCEED(MessageConst.ITEM_REGISTER_SUCCEED),
	
	/* 登録失敗 **/
	FAILED(MessageConst.ITEM_REGISTER_FAILED);
	
	/* メッセージID **/
	private String messageId;
}
