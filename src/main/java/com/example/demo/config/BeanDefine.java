package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * アプリケーション共通のBean定義クラス
 * 
 * セキュリティ用途で使用するPasswordEncoder（BCrypt）を定義
 * 他のクラスでDI（依存性注入）して利用可能
 * 
 * @author kajiwara_takuya
 */
@Configuration
public class BeanDefine {
	
	/**
     * パスワードエンコーダーのBean定義
     * 
     * ユーザーのパスワードをハッシュ化するためのエンコーダー
     * BCryptアルゴリズムを使用、セキュリティ性が高い
     * 
     * @return BCryptPasswordEncoderのインスタンス
     */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
