package vn.triumphstudio.clothesshop.service;

import vn.triumphstudio.clothesshop.domain.entity.OrderEntity;
import vn.triumphstudio.clothesshop.domain.enumration.OrderStatus;
import vn.triumphstudio.clothesshop.domain.request.OrderRequest;

import java.util.List;

public interface OrderService {
    OrderEntity placeOrder(long userId, OrderRequest request);

    List<OrderEntity> getUserOrders(long userId);

    OrderEntity cancelUserOrder(long userId, long orderId);

    OrderEntity getOrderById(long orderId);

    List<OrderEntity> getAllOrders();

    void changeOrderStatus(long orderId, OrderStatus status);
}
