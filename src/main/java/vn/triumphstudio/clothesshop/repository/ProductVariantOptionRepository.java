package vn.triumphstudio.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.triumphstudio.clothesshop.domain.entity.ProductVariantOptionEntity;

@Repository
public interface ProductVariantOptionRepository extends JpaRepository<ProductVariantOptionEntity, Long> {
}
