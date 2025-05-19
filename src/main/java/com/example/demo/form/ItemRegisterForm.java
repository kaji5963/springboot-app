package com.example.demo.form;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 商品登録Formクラス
 * 
 * @author kajiwara_takuya
 *
 */
@Data
public class ItemRegisterForm {

	/** 商品ID */
	@Id
	@Column(name = "item_id")
	private String itemId;

	/** 商品名 */
	@Column(name = "item_name")
	private String itemName;

	/** 単価 */
	private int price;

	/** 入荷日 */
	@Column(name = "arrival_date")
	private LocalDate arrivalDate;
}