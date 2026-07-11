package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.AuthorEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

  Optional<AuthorEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<AuthorEntity> findByCreatedBy(Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
