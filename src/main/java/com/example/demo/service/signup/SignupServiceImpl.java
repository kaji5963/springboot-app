package com.example.demo.service.signup;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.SignupResult;
import com.example.demo.constant.db.AuthorityKind;
import com.example.demo.constant.db.UserStatusKind;
import com.example.demo.dto.SignupInfo;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.service.common.MailSendService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 *  ユーザー登録画面 Service実装クラス
 *  
 *  @author kajiwara_takuya
 */
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	/** メール送信Serviceクラス */
	private final MailSendService mailSendService;

	/** ユーザー情報テーブルRepositoryクラス */
	private final UserInfoRepository repository;

	/** パスワードエンコーダー */
	private final PasswordEncoder passwordEncoder;

	/** メッセージソース */
	private final MessageSource messageSource;

	/** ワンタイムコード有効時間 */
	@Value("${one-time-code.valid-time}")
	private Duration oneTimeCodeValidTime = Duration.ZERO;

	/** ワンタイムコードの長さ */
	@Value("${one-time-code.length}")
	private int oneTimeCodeLength = 0;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignupResult signup(SignupInfo dto) {
		Optional<UserInfo> userInfoOpt = repository.findById(dto.getLoginId());
		
		// DBに既にデータが存在していた場合に処理終了
		if (userInfoOpt.isPresent()) {
			UserInfo userInfo = userInfoOpt.get();
			
			if (userInfo.isSignupCompleted()) {
				return SignupResult.FAILURE_BY_ALREADY_COMPLETED;
			}

			return SignupResult.FAILURE_BY_SIGNUP_PROCEEDING;
		}

		// ランダム数字４桁の文字列を作成
		String oneTimeCode = generateOneTimeCode();
		
		// ユーザー登録情報を作成
		UserInfo signupInfo = editSignupInfo(dto, oneTimeCode);
		
		try {
			repository.save(signupInfo);
		} catch (Exception e) {
			return SignupResult.FAILURE_BY_DB_ERROR;
		}

		// メール用のメッセージ本文を取得
		String mailTextBase = AppUtil.getMessage(messageSource, MessageConst.SIGNUP_MAIL_TEXT);
		
		// 本文の置換変数に値をセットする（ワンタイムコード：４桁の数字、有効期限：３分間）
		String mailText = MessageFormat.format(mailTextBase, oneTimeCode, oneTimeCodeValidTime.toMinutes());
		
		// メール用の件名を取得
		String mailSubject = AppUtil.getMessage(messageSource, MessageConst.SIGNUP_MAIL_SUBJECT);
		
		// メール送信の成功or失敗
		boolean canSendMail = mailSendService.sendMail(dto.getMailAddress(), mailSubject, mailText);
		
		// メール送信の成功or失敗を判定
		if (!canSendMail) {
			// 仮登録されたユーザーを削除
			boolean isDeleteFailure = deleteSignupInfo(dto.getLoginId());
			
			if (!isDeleteFailure) {
				return SignupResult.FAILURE_BY_DB_ERROR;
			}
			return SignupResult.FAILURE_BY_MAIL_SEND_ERROR;
		}

		return SignupResult.SUCCEED;
	}

	/**
	 * ランダムな数字でワンタイムコードを生成します。
	 * 
	 * @return ワンタイムコード
	 */
	private String generateOneTimeCode() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < oneTimeCodeLength; i++) {
			int randomNum = ThreadLocalRandom.current().nextInt(10);
			sb.append(randomNum);
		}

		return sb.toString();
	}

	/**
	 * ユーザー登録情報を作成する。
	 * 
	 * @param dto ユーザー登録画面Service入力情報
	 * @param oneTimeCode ワンタイムコード
	 * @return ユーザー登録情報
	 */
	private UserInfo editSignupInfo(SignupInfo dto, String oneTimeCode) {
		// UserInfoクラスをインスタンス化
		UserInfo userInfo = new UserInfo();
		
		// 登録情報をそれぞれ格納
		userInfo.setLoginId(dto.getLoginId());
		userInfo.setPassword(passwordEncoder.encode(dto.getPassword()));
		userInfo.setMailAddress(dto.getMailAddress());
		userInfo.setOneTimeCode(passwordEncoder.encode(oneTimeCode));
		userInfo.setOneTimeCodeSendTime(LocalDateTime.now());
		userInfo.setUserStatusKind(UserStatusKind.ENABLED);
		userInfo.setAuthorityKind(AuthorityKind.ITEM_WATCHER);
		userInfo.setSignupCompleted(false);
		userInfo.setCreateTime(LocalDateTime.now());
		userInfo.setUpdateTime(LocalDateTime.now());
		userInfo.setUpdateUser(dto.getLoginId());

		return userInfo;
	}

	/**
	 * DBのユーザー登録情報から対象のログインIDに紐づくレコードを削除します。
	 * 
	 * @param loginId ログインID
	 * @return 結果ステータス(成功ならtrue)
	 */
	private boolean deleteSignupInfo(String loginId) {
		try {
			repository.deleteById(loginId);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
