package vn.triumphstudio.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.triumphstudio.clothesshop.domain.entity.OrderEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, PagingAndSortingRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUser_IdOrderByCreatedAtDesc(long userId);

    OrderEntity findFirstByUser_IdAndId(long userId, long id);
}
