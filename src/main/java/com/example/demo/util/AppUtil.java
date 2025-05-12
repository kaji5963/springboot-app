package com.example.demo.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * アプリケーション共通クラス
 * 
 * @author kajiwara_takuya
 */
public class AppUtil {
	
	/**
	 * メッセージキーからメッセージを取得
	 * 
	 * @param messageSource メッセージソース
	 * @param key メッセージキー
	 * @param params 置換文字(省略可能)
	 * @return メッセージ
	 */
	public static String getMessage(MessageSource messageSource, String key, Object... params) {
		return messageSource.getMessage(key, params, Locale.JAPAN);
	}
	
	/**
	 * DBのLIKE検索用にパラメーターにワイルドカードを付与します。
	 * 
	 * @param param パラメーター
	 * @return 前後にワイルドカードが付いたパラメーター
	 */
	public static String addWildCard(String param) {
		return "%" + param + "%";
	}
}
