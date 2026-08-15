package com.tarosuke777.hms.specification;

import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.entity.TaskEntity_;
import com.tarosuke777.hms.enums.TaskCategory;
import com.tarosuke777.hms.enums.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {
  public static Specification<TaskEntity> withFilters(Integer userId, TaskStatus status,
      TaskCategory category) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(cb.equal(root.get(TaskEntity_.createdBy), userId));

      if (status != null) {
        predicates.add(cb.equal(root.get(TaskEntity_.status), status));
      }

      if (category != null) {
        predicates.add(cb.equal(root.get(TaskEntity_.category), category));
      }

      // ソート条件：未設定(NULL)を一番上にし、その後に重要度・緊急度が高い順（降順）で並べる
      if (query != null) {
        query.orderBy(
            // importanceがNULLなら 1 (上)、値があれば 0 として並べ替え、さらにその中で降順にする
            cb.asc(cb.selectCase().when(root.get(TaskEntity_.importance).isNull(), 0).otherwise(1)),
            cb.asc(root.get(TaskEntity_.importance)),
            // urgencyも同様にNULLを上にする
            cb.asc(cb.selectCase().when(root.get(TaskEntity_.urgency).isNull(), 0).otherwise(1)),
            cb.asc(root.get(TaskEntity_.urgency)));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
