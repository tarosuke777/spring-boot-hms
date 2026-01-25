package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    @EntityGraph(attributePaths = {"author"})
    Optional<BookEntity> findByBookIdAndCreatedBy(Integer bookId, String createdBy);

    @EntityGraph(attributePaths = {"author"})
    List<BookEntity> findByCreatedByOrderByBookIdAsc(String createdBy);

    boolean existsByBookIdAndCreatedBy(Integer bookId, String createdBy);
}
