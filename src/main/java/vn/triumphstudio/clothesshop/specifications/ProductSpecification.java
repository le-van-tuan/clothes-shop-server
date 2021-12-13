package vn.triumphstudio.clothesshop.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;

import java.util.List;

public class ProductSpecification {
    public static Specification<ProductEntity> byDeletedStatus(boolean includeDeleted) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (includeDeleted) {
                return criteriaBuilder.or(criteriaBuilder.equal(root.get("deleted"), false), criteriaBuilder.equal(root.get("deleted"), true));
            }
            return criteriaBuilder.equal(root.get("deleted"), false);
        };
    }

    public static Specification<ProductEntity> byPublishedStatus(Boolean published) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (published == null) {
                return criteriaBuilder.or(criteriaBuilder.equal(root.get("published"), true), criteriaBuilder.equal(root.get("published"), false));
            }
            return criteriaBuilder.equal(root.get("published"), published);
        };
    }

    public static Specification<ProductEntity> byCategories(List<Long> categories) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return criteriaBuilder.and();
            }
            return root.get("category_id").in(categories);
        };
    }
}
