package com.tarosuke777.hms.specification;

import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.entity.TaskEntity_;
import com.tarosuke777.hms.enums.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {
  public static Specification<TaskEntity> withFilters(Integer userId, TaskStatus status) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(cb.equal(root.get(TaskEntity_.createdBy), userId));

      if (status != null) {
        predicates.add(cb.equal(root.get(TaskEntity_.status), status));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
