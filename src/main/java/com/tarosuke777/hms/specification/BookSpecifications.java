package com.tarosuke777.hms.specification;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.tarosuke777.hms.entity.AuthorEntity_;
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.entity.BookEntity_;
import com.tarosuke777.hms.enums.BookGenre;
import jakarta.persistence.criteria.Predicate;

public class BookSpecifications {

    public static Specification<BookEntity> withFilters(Integer userId, BookGenre genre,
            Integer authorId, Boolean isAdult) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(BookEntity_.createdBy), userId));

            if (genre != null) {
                predicates.add(cb.equal(root.get(BookEntity_.genre), genre));
            }

            if (authorId != null) {
                predicates.add(
                        cb.equal(root.get(BookEntity_.author).get(AuthorEntity_.id), authorId));
            }

            if (Boolean.TRUE.equals(isAdult)) {
                predicates.add(cb.isTrue(root.get(BookEntity_.isAdult)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
