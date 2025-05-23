package com.example.demo.constant;

/**
 * URL 定義クラス
 * 
 * @author kajiwara-takuya
 */
public class UrlConst {

	/** ログイン画面 */
	public static final String LOGIN = "/login";
	
	/** ユーザー登録画面 */
	public static final String SIGNUP = "/signup";
	
	/** ユーザー登録情報確認画面 */
	public static final String SIGNUP_CONFIRM = "/signupConfirm";

	/** ユーザー登録情報確認結果画面 */
	public static final String SIGNUP_COMPLETION = "/signupCompletion";
	
	/** メニュー画面 */
	public static final String MENU = "/menu";
	
	/** ユーザー一覧画面 */
	public static final String USER_LIST = "/userList";
	
	/** ユーザー編集画面 */
	public static final String USER_EDIT = "/userEdit";
	
	/** 商品一覧画面 */
	public static final String ITEM_LIST = "/itemList";
	
	/** 商品登録画面 */
	public static final String ITEM_REGISTER = "/itemRegister";

	/** 商品編集画面 */
	public static final String ITEM_EDIT = "/itemEdit";
	
	/** 認証不要画面 */
	public static final String[] NO_AUTHENTICATION =  { LOGIN, SIGNUP, SIGNUP_CONFIRM, SIGNUP_COMPLETION, "/webjars/**", "/", "/css/**" };
}
