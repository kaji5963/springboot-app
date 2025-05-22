
package com.example.demo.service.item;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ItemInfo;
import com.example.demo.repository.ItemInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー編集画面Service実装クラス
 * 
 * @author kajiwara_takuya
 */
@Service
@RequiredArgsConstructor
public class ItemEditServiceImpl implements ItemEditService {

	/** ユーザー情報テーブルRepository */
	private final ItemInfoRepository repository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<ItemInfo> searchItemInfo(String itemId) {
		return repository.findById(itemId);
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
//	public UserEditResult updateUserInfo(UserUpdateInfo userUpdateInfo) {
//		// UserEditResultクラスをインスタンス化
//		UserEditResult userUpdateResult = new UserEditResult();
//
//		// 現在の登録情報を取得
//		Optional<UserInfo> updateInfoOpt = repository.findById(userUpdateInfo.getLoginId());
//		
//		// データの存在チェック
//		if (updateInfoOpt.isEmpty()) {
//			// 存在しない場合、FAILED情報を返す
//			userUpdateResult.setUpdateMessage(UserEditMessage.FAILED);
//			return userUpdateResult;
//		}
//
//		// 画面の入力情報等をセット
//		UserInfo updateInfo = updateInfoOpt.get();
//		
//		// 更新情報をそれぞれ格納
//		updateInfo.setUserStatusKind(userUpdateInfo.getUserStatusKind());
//		updateInfo.setAuthorityKind(userUpdateInfo.getAuthorityKind());
//		
//		// チェックボックスがチェックされていた場合true
//		if (userUpdateInfo.isResetsLoginFailure()) {
//			// それぞれ初期値にして格納
//			updateInfo.setLoginFailureCount(0);
//			updateInfo.setAccountLockedTime(null);
//		}
//		
//		// 更新情報をそれぞれ格納
//		updateInfo.setUpdateTime(LocalDateTime.now());
//		updateInfo.setUpdateUser(userUpdateInfo.getUpdateUserId());
//
//		// 更新情報を含むEntityをDBに保存（既存レコードがあれば更新）
//		try {
//			repository.save(updateInfo);
//		} catch (Exception e) {
//			userUpdateResult.setUpdateMessage(UserEditMessage.FAILED);
//			return userUpdateResult;
//		}
//
//		userUpdateResult.setUpdateUserInfo(updateInfo);
//		userUpdateResult.setUpdateMessage(UserEditMessage.SUCCEED);
//		
//		return userUpdateResult;
//	}

}
