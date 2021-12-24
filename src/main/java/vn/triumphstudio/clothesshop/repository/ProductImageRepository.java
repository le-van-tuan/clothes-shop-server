package vn.triumphstudio.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.triumphstudio.clothesshop.domain.entity.ProductImageEntity;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    @Modifying
    @Query("DELETE FROM ProductImageEntity f WHERE f.id = ?1")
    void deleteById(long id);
}
