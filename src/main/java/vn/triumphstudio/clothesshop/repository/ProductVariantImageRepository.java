package vn.triumphstudio.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.triumphstudio.clothesshop.domain.entity.ProductVariantImageEntity;

@Repository
public interface ProductVariantImageRepository extends JpaRepository<ProductVariantImageEntity, Long> {

    @Modifying
    @Query("DELETE FROM ProductVariantImageEntity f WHERE f.id = ?1")
    void deleteById(long id);
}
