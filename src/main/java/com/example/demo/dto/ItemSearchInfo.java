package com.example.demo.dto;

import lombok.Data;

/**
 * ユーザー一覧画面検索用DTOクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Data
public class ItemSearchInfo {

	/** 商品名 */
	private String itemName;

	/** 入荷担当者 */
	private String chargePerson;

}