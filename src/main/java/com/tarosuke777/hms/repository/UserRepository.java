package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  // ログインユーザー検索用
  Optional<UserEntity> findByName(String name);
}
