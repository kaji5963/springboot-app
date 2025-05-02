package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserInfo;

/**
 * ユーザー情報テーブル Repository
 * user_infoにアクセス
 * Spring Data JPA により自動的に実装される
 * 
 * @author kajiwara_takuya
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

}
