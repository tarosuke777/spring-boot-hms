package com.tarosuke777.hms.specification;

import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.entity.BookEntity_; // 自動生成されたメタモデル
import com.tarosuke777.hms.enums.BookGenre;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecifications {

    public static Specification<BookEntity> withFilters(Integer userId, BookGenre genre,
            Boolean isAdult) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getResultType() != Long.class) {
                root.fetch(BookEntity_.author, JoinType.LEFT);
            }

            predicates.add(cb.equal(root.get(BookEntity_.createdBy), userId));

            if (genre != null) {
                predicates.add(cb.equal(root.get(BookEntity_.genre), genre));
            }

            if (Boolean.TRUE.equals(isAdult)) {
                predicates.add(cb.isTrue(root.get(BookEntity_.isAdult)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
