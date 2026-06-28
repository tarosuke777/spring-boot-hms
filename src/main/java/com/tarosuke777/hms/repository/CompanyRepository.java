package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.CompanyEntity;

@Repository
public interface CompanyRepository
        extends JpaRepository<CompanyEntity, Integer>, JpaSpecificationExecutor<CompanyEntity> {

    @Override
    @NonNull
    List<CompanyEntity> findAll(@Nullable Specification<CompanyEntity> spec);

    @Override
    @NonNull
    Page<CompanyEntity> findAll(@Nullable Specification<CompanyEntity> spec, @NonNull Pageable pageable);

    Optional<CompanyEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

    List<CompanyEntity> findByCreatedBy(Integer createdBy);

    boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);

    Optional<CompanyEntity> findByName(String name);

}
