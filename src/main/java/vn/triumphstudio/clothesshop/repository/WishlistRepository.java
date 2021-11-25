package vn.triumphstudio.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.triumphstudio.clothesshop.domain.entity.WishlistEntity;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    WishlistEntity findFirstByUser_IdAndId(long userId, long wishlistId);
}
