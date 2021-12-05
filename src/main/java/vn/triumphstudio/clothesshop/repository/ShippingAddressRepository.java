package vn.triumphstudio.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.triumphstudio.clothesshop.domain.entity.ShippingAddressEntity;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddressEntity, Long> {
    List<ShippingAddressEntity> findAllByUser_IdOrderByCreatedAtDesc(long userId);

    ShippingAddressEntity findFirstByUser_IdAndId(long userId, long addressId);

    @Modifying(flushAutomatically = true)
    @Query(value = "update shipping_addresses set default_address = false where user_id = :userId", nativeQuery = true)
    @Transactional
    void markAsUnDefaultShippingAddress(@Param("userId") long userId);
}
