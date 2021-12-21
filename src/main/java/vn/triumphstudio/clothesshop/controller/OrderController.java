package vn.triumphstudio.clothesshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.triumphstudio.clothesshop.domain.entity.OrderEntity;
import vn.triumphstudio.clothesshop.domain.request.OrderRequest;
import vn.triumphstudio.clothesshop.service.OrderService;
import vn.triumphstudio.clothesshop.util.SecurityContextUtil;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/place")
    public OrderEntity placeOrder(@RequestBody OrderRequest request) {
        return this.orderService.placeOrder(SecurityContextUtil.getCurrentUser().getId(), request);
    }

    @GetMapping
    public List<OrderEntity> getMyOrder() {
        return this.orderService.getUserOrders(SecurityContextUtil.getCurrentUser().getId());
    }

    @PatchMapping(value = "/{id}")
    public OrderEntity cancelOrder(@PathVariable long id) {
        return this.orderService.cancelUserOrder(SecurityContextUtil.getCurrentUser().getId(), id);
    }
}
