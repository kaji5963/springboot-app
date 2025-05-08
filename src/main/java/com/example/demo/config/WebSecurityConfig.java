package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// SpringBoot Securityの設定変更（loginページ遷移、パラメーター変更、リダイレクト先変更）
		http.formLogin(
				login -> login.loginPage("/login")
						.usernameParameter("loginId")
						.defaultSuccessUrl("/menu"));

		return http.build();
	}
}
