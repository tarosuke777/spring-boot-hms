package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.BookEntity;

@Repository
public interface BookRepository
        extends JpaRepository<BookEntity, Integer>, JpaSpecificationExecutor<BookEntity> {

    @EntityGraph(attributePaths = {"author"})
    @Override
    @NonNull
    Page<BookEntity> findAll(@Nullable Specification<BookEntity> spec, @NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"author"})
    Optional<BookEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

    @EntityGraph(attributePaths = {"author"})
    List<BookEntity> findByCreatedByOrderByIdAsc(Integer createdBy);

    boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
