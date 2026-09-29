package arpa.home.hms.repository;

import arpa.home.hms.entity.BusinessEntity;
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

@Repository
public interface BusinessRepository
    extends JpaRepository<BusinessEntity, Integer>, JpaSpecificationExecutor<BusinessEntity> {

  @Override
  @NonNull
  List<BusinessEntity> findAll(@Nullable Specification<BusinessEntity> spec);

  @Override
  @NonNull
  Page<BusinessEntity> findAll(@Nullable Specification<BusinessEntity> spec,
      @NonNull Pageable pageable);

  Optional<BusinessEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<BusinessEntity> findByCreatedBy(Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
