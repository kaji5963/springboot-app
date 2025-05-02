package com.example.demo.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
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
	
	  /**
     * DozerマッパーのBean定義
     * 
     * SignupForm → UserInfo などのDTOとEntity間のオブジェクト変換を行うために使用
     * SpringのDIコンテナに登録することで、Serviceなどで@Autowiredや@RequiredArgsConstructorで利用可能
     *
     * @return Dozer用のMapperインスタンス
     */
	@Bean
	Mapper mapper() {
		return new DozerBeanMapper();
	}
}
