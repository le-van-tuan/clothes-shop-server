package vn.triumphstudio.clothesshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.triumphstudio.clothesshop.domain.entity.OrderEntity;
import vn.triumphstudio.clothesshop.domain.entity.OrderItemEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductVariantEntity;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.domain.enumration.OrderStatus;
import vn.triumphstudio.clothesshop.domain.request.OrderItemRequest;
import vn.triumphstudio.clothesshop.domain.request.OrderRequest;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.repository.OrderRepository;
import vn.triumphstudio.clothesshop.service.OrderService;
import vn.triumphstudio.clothesshop.service.ProductService;
import vn.triumphstudio.clothesshop.service.UserService;
import vn.triumphstudio.clothesshop.util.SecurityContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderEntity placeOrder(long userId, OrderRequest request) {
        UserEntity user = this.userService.getUserById(userId);

        OrderEntity order = new OrderEntity();
        order.setUser(user);

        order.setCity(request.getShippingAddress().getCity());
        order.setDistrict(request.getShippingAddress().getDistrict());
        order.setWard(request.getShippingAddress().getWard());
        order.setAddress(request.getShippingAddress().getAddress());
        order.setName(user.getName());
        order.setEmail(user.getEmail());
        order.setPhoneNumber(request.getShippingAddress().getPhoneNumber());

        double subTotal = 0;
        double total;
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            OrderItemEntity item = new OrderItemEntity();

            ProductVariantEntity variant = this.productService.getProductVariantById(itemRequest.getVariantId());
            item.setOrder(order);
            item.setProduct(this.productService.getProductById(itemRequest.getProductId()));
            item.setVariant(variant);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(variant.getPrice());

            subTotal += itemRequest.getQuantity() * variant.getPrice();
            orderItems.add(item);
        }
        total = subTotal + request.getShippingFee();
        order.setOrderItems(orderItems);
        order.setShippingFee(request.getShippingFee());
        order.setSubTotal(subTotal);
        order.setTotal(total);

        order.setOrderStatus(OrderStatus.Pending);
        order.setMessage(request.getMessage());
        order.setOrderNumber(this.getRandomNumberString());

        return this.orderRepository.save(order);
    }

    public String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999999);
        return String.format("%06d", number);
    }

    @Override
    public List<OrderEntity> getUserOrders(long userId) {
        return this.orderRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);
    }

    @Override
    public OrderEntity cancelUserOrder(long userId, long orderId) {
        OrderEntity order = this.orderRepository.findFirstByUser_IdAndId(userId, orderId);
        if (order == null) throw new BusinessLogicException("No order found with id = " + orderId);
        if (OrderStatus.Cancelled.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Cancelled, you can not cancel");
        if (OrderStatus.Declined.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Declined, you can not cancel");
        if (OrderStatus.Shipped.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Shipped, you can not cancel");
        if (OrderStatus.Completed.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Completed, you can not cancel");

        order.setOrderStatus(OrderStatus.Cancelled);
        return this.orderRepository.save(order);
    }

    @Override
    public OrderEntity getOrderById(long orderId) {
        return this.orderRepository.findById(orderId).orElseThrow(() -> new BusinessLogicException("No order found with id = " + orderId));
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        Sort sort = Sort.by("createdAt").descending();
        return this.orderRepository.findAll(sort);
    }

    @Override
    public void changeOrderStatus(long orderId, OrderStatus status) {
        OrderEntity order = this.getOrderById(orderId);
        if (OrderStatus.Cancelled.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Cancelled, you cannot change status");
        if (OrderStatus.Declined.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Declined, you cannot change status");
        if (OrderStatus.Completed.equals(order.getOrderStatus()))
            throw new BusinessLogicException("Order was Completed, you cannot change status");
        order.setOrderStatus(status);
        order.setUpdateBy(this.userService.getUserById(SecurityContextUtil.getCurrentUser().getId()));
        this.orderRepository.save(order);
    }
}
