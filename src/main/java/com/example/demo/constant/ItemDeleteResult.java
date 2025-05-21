package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 処理結果種別
 * 
 * @author kajiwara_takuya
 */
@Getter
@AllArgsConstructor
public enum ItemDeleteResult {

	/* エラーなし **/
	SUCCEED(MessageConst.ITEM_ID_DELETE_SUCCEED),
	
	/* エラーあり **/
	ERROR(MessageConst.NON_EXISTED_ITEM_ID);
	
	/* メッセージID **/
	private String messageId;
}
