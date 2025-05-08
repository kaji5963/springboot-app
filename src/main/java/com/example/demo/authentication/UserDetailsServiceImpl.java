package com.example.demo.authentication;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー情報生成
 * 
 * @author kajiwara_takuya
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	/** ユーザー情報テーブルRepository */
	private final UserInfoRepository repository;

	/** アカウントロックを行うログイン失敗回数境界値 */
	private final int LOCKING_BORDER_COUNT = 3;

	/** アカウントロックの継続時間 */
	private final int LOCKING_TIME = 1;

	/**
	 * ユーザー情報生成
	 * 
	 * @param username ログインID
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo userInfo = repository.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		LocalDateTime accountLockedTime = userInfo.getAccountLockedTime();
		boolean isAccountLocked = accountLockedTime != null
				&& accountLockedTime.plusHours(LOCKING_TIME).isAfter(LocalDateTime.now());

		UserDetails userDetail = User.withUsername(userInfo.getLoginId())
				.password(userInfo.getPassword())
				.roles("USER")
				.disabled(userInfo.isDisabled())
				.accountLocked(isAccountLocked)
				.build();

		return userDetail;
	}

	/**
	 * 認証失敗時にログイン失敗回数を加算、ロック日時を更新する
	 * 
	 * @param event イベント情報
	 */
	@EventListener
	public void handle(AuthenticationFailureBadCredentialsEvent event) {
		String loginId = event.getAuthentication().getName();
		repository.findById(loginId).ifPresent(userInfo -> {
			repository.save(userInfo.incrementLoginFailureCount());

			boolean isReachFailureCount = userInfo.getLoginFailureCount() == LOCKING_BORDER_COUNT;

			if (isReachFailureCount) {
				repository.save(userInfo.updateAccountLocked());
			}
		});
	}

	/**
	 * 認証成功時にログイン失敗回数をリセットする
	 * 
	 * @param event イベント情報
	 */
	@EventListener
	public void handle(AuthenticationSuccessEvent event) {
		var loginId = event.getAuthentication().getName();
		repository.findById(loginId).ifPresent(userInfo -> {
			repository.save(userInfo.resetLoginFailureInfo());
		});
	}
}
