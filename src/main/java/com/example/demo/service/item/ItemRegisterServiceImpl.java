
package com.example.demo.service.item;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.constant.ItemRegisterMessage;
import com.example.demo.dto.ItemRegisterResult;
import com.example.demo.entity.ItemInfo;
import com.example.demo.form.ItemRegisterForm;
import com.example.demo.repository.ItemInfoRepository;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

/**
 * 商品登録画面Service実装クラス
 * 
 * @author kajiwara_takuya
 */
@Service
@RequiredArgsConstructor
public class ItemRegisterServiceImpl implements ItemRegisterService {

	/** Repositoryクラス */
	private final ItemInfoRepository repository;
	
	/** Dozer Mapper */
	private final Mapper mapper;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemRegisterResult itemRegister(ItemRegisterForm form, String loginId) {
		// 登録結果オブジェクトをインスタンス化
		ItemRegisterResult result = new ItemRegisterResult();
		
		// フォーム情報から商品情報を取得
		Optional<ItemInfo> itemInfoOpt = repository.findById(form.getItemId());
		
		// フォーム情報をitemInfoクラスへマッピング
		ItemInfo itemInfo = mapper.map(form, ItemInfo.class);
		
		// フォームのitemIdがDBに存在した場合
		if (!itemInfoOpt.isEmpty()) {
			// 登録結果「失敗」を格納
			result.setRegisterMessage(ItemRegisterMessage.FAILED);
			
			result.setItemInfo(itemInfo);
			
			return result;
		}
		
		// フォーム情報にないデータのみをsetterで格納
		itemInfo.setChargePerson(loginId); 
		itemInfo.setCreateTime(LocalDateTime.now());
		itemInfo.setUpdateTime(LocalDateTime.now());
		itemInfo.setUpdateUser(loginId);
		result.setItemInfo(itemInfo);

		// DBへ登録
		repository.save(itemInfo);
		
		// 登録結果「成功」を格納
		result.setRegisterMessage(ItemRegisterMessage.SUCCEED);
		
		return result;
	}
}
