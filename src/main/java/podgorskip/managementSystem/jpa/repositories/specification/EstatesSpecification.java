package podgorskip.managementSystem.jpa.repositories.specification;

import org.springframework.data.jpa.domain.Specification;
import podgorskip.managementSystem.jpa.entities.Estate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.criteria.Predicate;

public class EstatesSpecification {
    public static Specification<Estate> filteredEstates(String type, Integer priceFrom, Integer priceTo,
                                                        Integer rooms, Integer bathrooms, String localization,
                                                        Boolean garage, Integer storeys) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(type)) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (Objects.nonNull(priceFrom)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom));
            }

            if (Objects.nonNull(priceTo)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
            }

            if (Objects.nonNull(rooms)) {
                predicates.add(criteriaBuilder.equal(root.get("rooms"), rooms));
            }

            if (Objects.nonNull((bathrooms))) {
                predicates.add(criteriaBuilder.equal(root.get("bathrooms"), bathrooms));
            }

            if (Objects.nonNull(localization)) {
                predicates.add(criteriaBuilder.equal(root.get("localization"), localization));
            }

            if (Objects.nonNull(garage)) {
                predicates.add(criteriaBuilder.equal(root.get("garage"), garage));
            }

            if (Objects.nonNull(storeys)) {
                predicates.add(criteriaBuilder.equal(root.get("storeys"), storeys));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        });
    }
}
