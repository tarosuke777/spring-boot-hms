package com.tarosuke777.hms.specification;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.tarosuke777.hms.entity.CompanyEntity;
import com.tarosuke777.hms.entity.CompanyEntity_;
import jakarta.persistence.criteria.Predicate;

public class CompanySpecifications {

    public static Specification<CompanyEntity> withFilters(Integer userId, String name) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(CompanyEntity_.createdBy), userId));

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(root.get(CompanyEntity_.name), "%" + name + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
