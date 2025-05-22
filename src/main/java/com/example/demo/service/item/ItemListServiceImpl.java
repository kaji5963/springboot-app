package com.example.demo.service.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.constant.ItemDeleteResult;
import com.example.demo.dto.ItemSearchInfo;
import com.example.demo.dto.StaffInfo;
import com.example.demo.entity.ItemInfo;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.ItemInfoRepository;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * 商品一覧画面Service実装クラス
 * 
 * @author kajiwara_takuya
 *
 */
@Service
@RequiredArgsConstructor
public class ItemListServiceImpl implements ItemListService {

	/** ユーザー情報テーブルDAO */
	private final UserInfoRepository userInfoRepository;

	/** 商品情報テーブルDAO */
	private final ItemInfoRepository itemInfoRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StaffInfo> obtainUserIdList() {
		// ユーザー情報を全て取得
		List<UserInfo> userInfos = userInfoRepository.findAll();

		// StaffInfoリストを初期化（空のリストを作成）
		List<StaffInfo> staffInfos = new ArrayList<>();

		for (UserInfo userInfo : userInfos) {
			StaffInfo staffInfo = new StaffInfo();

			// ユーザーID、ユーザー名を格納し空リストへ追加
			staffInfo.setUserId(userInfo.getLoginId());
			staffInfo.setUserName(userInfo.getUserName());
			staffInfos.add(staffInfo);
		}

		return staffInfos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ItemInfo> editItemListByParam(ItemSearchInfo dto) {
		String itemName = dto.getItemName();
		String chargePerson = dto.getChargePerson();
		boolean hasItemName = StringUtils.hasText(itemName);
		boolean hasChargePerson = StringUtils.hasText(chargePerson);

		// 商品名及び入荷担当者での検索
		if (hasItemName && hasChargePerson) {
			return itemInfoRepository.findByItemNameLikeAndChargePerson("%" + itemName + "%", chargePerson);
		}

		// 商品名での検索
		if (hasItemName) {
			return itemInfoRepository.findByItemNameLike("%" + itemName + "%");
		}

		// 入荷担当者での検索
		if (hasChargePerson) {
			return itemInfoRepository.findByChargePerson(chargePerson);
		}

		// 全件検索
		return itemInfoRepository.findAll();
	}
	
	/**
	 * 選択された商品IDから商品を削除します。
	 * 
	 * @param itemId 選択された商品ID
	 */
	public ItemDeleteResult deleteItemInfoById(String itemId) {
		// 商品IDがDBに存在するか確認
		Optional<ItemInfo> itemInfo = itemInfoRepository.findById(itemId);
		
		// 商品IDが存在しなかった場合
		if (itemInfo.isEmpty()) {
			return ItemDeleteResult.ERROR;
		}
		
		itemInfoRepository.deleteById(itemId);
		
		return ItemDeleteResult.SUCCEED;
	}
}