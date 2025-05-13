package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザー更新結果メッセージEnumクラス
 * 
 * @author kajiwara_takuya
 */
@Getter
@AllArgsConstructor
public enum UserEditMessage {

	/* 更新成功 **/
	SUCCEED(MessageConst.USEREDIT_UPDATE_SUCCEED),
	
	/* 更新失敗 **/
	FAILED(MessageConst.USEREDIT_UPDATE_FAILED);
	
	/* メッセージID **/
	private String messageId;
}
