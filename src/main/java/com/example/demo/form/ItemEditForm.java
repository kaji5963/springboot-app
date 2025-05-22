package com.example.demo.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

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
public class ItemEditForm {

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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrivalDate;
}