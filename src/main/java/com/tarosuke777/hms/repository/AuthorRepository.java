package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.AuthorEntity;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    Optional<AuthorEntity> findByAuthorIdAndCreatedBy(Integer authorId, String createdBy);

    List<AuthorEntity> findByCreatedBy(String createdBy);

    boolean existsByAuthorIdAndCreatedBy(Integer authorId, String createdBy);
}
