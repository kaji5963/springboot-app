package com.example.demo.dto;

import lombok.Data;

/**
 * 従業員情報
 * 
 * @author kajiwara_takuya
 */
@Data
public class StaffInfo {

	/* ユーザーID **/
	private String userId;

	/* ユーザー名 **/
	private String userName;
}