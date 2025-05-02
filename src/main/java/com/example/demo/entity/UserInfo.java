package com.example.demo.entity;

import org.dozer.Mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * ユーザー情報テーブル Entity
 * 
 * @author kajiwara_takuya
 */
@Entity
@Table(name = "user_info")
@Data
public class UserInfo {

	/** ログインID */
	@Id
	@Column(name = "login_id")
	@Mapping("loginId") //Dozerマッピング用
	private String loginId;

	/** パスワード */ // Dozerマッピング除外
	private String password;
}
