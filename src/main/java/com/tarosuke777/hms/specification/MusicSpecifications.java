package com.tarosuke777.hms.specification;

import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.entity.MusicEntity_;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class MusicSpecifications {

  public static Specification<MusicEntity> withFilters(Integer userId) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(cb.equal(root.get(MusicEntity_.createdBy), userId));

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
