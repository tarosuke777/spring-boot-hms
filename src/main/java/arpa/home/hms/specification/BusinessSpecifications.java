package arpa.home.hms.specification;

import arpa.home.hms.entity.BusinessEntity;
import arpa.home.hms.entity.BusinessEntity_;
import arpa.home.hms.enums.BusinessGenre;
import arpa.home.hms.enums.BusinessStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class BusinessSpecifications {

  public static Specification<BusinessEntity> withFilters(Integer userId, String keyword,
      BusinessGenre genre, BusinessStatus status) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(cb.equal(root.get(BusinessEntity_.createdBy), userId));

      if (keyword != null && !keyword.isBlank()) {
        String pattern = "%" + keyword.trim() + "%";
        predicates.add(cb.or(cb.like(root.get(BusinessEntity_.name), pattern),
            cb.like(root.get(BusinessEntity_.overview), pattern)));
      }

      if (genre != null) {
        predicates.add(cb.equal(root.get(BusinessEntity_.genre), genre));
      }

      if (status != null) {
        predicates.add(cb.equal(root.get(BusinessEntity_.status), status));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
