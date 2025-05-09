package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザー状態種別
 * 
 * @author kajiwara_takuya
 */
@Getter
@AllArgsConstructor
public enum UserStatusKind {

	/** 利用可能 */
	ENABLED(false, "利用可能"),
	
	/** 利用不可 */
	DISABLED(true, "利用不可");
	
	/** DB登録値 */
	private boolean isDisabled;
	
	/** 画面表示する説明文 */
	private String displayValue;
}
