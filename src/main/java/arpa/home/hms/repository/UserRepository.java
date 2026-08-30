package arpa.home.hms.repository;

import arpa.home.hms.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  // ログインユーザー検索用
  Optional<UserEntity> findByName(String name);
}
