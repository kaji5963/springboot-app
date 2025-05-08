package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.constant.UrlConst;

/**
 * Spring Securityカスタマイズ用
 * 
 * @author kajiwara_takuya
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	
	/** ユーザー名のname属性 */
	private final String USERNAME_PARAMETER = "loginId";

	/**
	 * Spring Securityカスタマイズ用
	 * 
	 * @param http カスタマイズパラメータ
	 * @return カスタマイズ結果
	 * @throws Exception 予期せぬ例外
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				authorize -> authorize
						.requestMatchers(UrlConst.NO_AUTHENTICATION).permitAll()
						.anyRequest().authenticated())
				.formLogin(
						login -> login.loginPage(UrlConst.LOGIN) // 自作ログイン(Controller)を行うための設定
								.usernameParameter(USERNAME_PARAMETER) // ユーザー名パラメーターのname属性
								.defaultSuccessUrl(UrlConst.MENU)); // ログイン成功後のレンダリングURL

		return http.build();
	}
}
