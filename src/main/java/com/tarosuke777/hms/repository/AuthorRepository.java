package com.tarosuke777.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.AuthorEntity;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
}
