
package com.example.demo.service.signup;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.SignupConfirmStatus;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録情報確認画面Service実装クラス
 * 
 * @author kajiwara_takuya
 *
 */
@Service
@RequiredArgsConstructor
public class SignupConfirmServiceImpl implements SignupConfirmService {

	/** ユーザー情報テーブルRepositoryクラス */
	private final UserInfoRepository repository;

	/** パスワードエンコーダー */
	private final PasswordEncoder passwordEncoder;

	/** ワンタイムコード有効時間 */
	@Value("${one-time-code.valid-time}")
	private Duration oneTimeCodeValidTime = Duration.ZERO;

	@Override
	public SignupConfirmStatus updateUserAsSignupCompletion(String loginId, String oneTimeCode) {
		Optional<UserInfo> updateInfoOpt = repository.findById(loginId);
		
		if (updateInfoOpt.isEmpty()) {
			return SignupConfirmStatus.FAILURE_BY_NOT_EXISTS_TENTATIVE_USER;
		}
		
		UserInfo updateInfo = updateInfoOpt.get();

		// ワンタイムコードのチェック：入力された値、DBに格納されている値
		if (!passwordEncoder.matches(oneTimeCode, updateInfo.getOneTimeCode())) {
			return SignupConfirmStatus.FAILURE_BY_WRONG_ONE_TIME_CODE;
		}

		// ワンタイムコードを送信した時間 ＋ 有効期間」＝ ワンタイムコードの有効期限の時刻
		LocalDateTime oneTimeCodeTimeLimit = updateInfo.getOneTimeCodeSendTime().plus(oneTimeCodeValidTime);
		
		// 有効期限内かを判定
		boolean isOneTimeCodeAvailable = oneTimeCodeTimeLimit.isAfter(LocalDateTime.now());
		
		if (!isOneTimeCodeAvailable) {
			return SignupConfirmStatus.FAILURE_BY_EXPIRED_ONE_TIME_CODE;
		}

		// ユーザー情報を格納
		updateInfo.setSignupCompleted(true);
		updateInfo.setOneTimeCode(null);
		updateInfo.setOneTimeCodeSendTime(null);
		updateInfo.setUpdateTime(LocalDateTime.now());
		updateInfo.setUpdateUser(loginId);
		
		// 本登録処理
		try {
			repository.save(updateInfo);
		} catch (Exception e) {
			return SignupConfirmStatus.FAILURE_BY_DB_ERROR;
		}

		return SignupConfirmStatus.SUCCEED;
	}

}
