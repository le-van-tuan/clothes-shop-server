package vn.triumphstudio.clothesshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.triumphstudio.clothesshop.domain.entity.ShippingAddressEntity;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.domain.request.ShippingAddressRequest;
import vn.triumphstudio.clothesshop.domain.request.UserProfileRequest;
import vn.triumphstudio.clothesshop.service.UserService;
import vn.triumphstudio.clothesshop.util.SecurityContextUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/addresses")
    public List<ShippingAddressEntity> getAllShippingAddresses() {
        return this.userService.getShippingAddressesByUserId(SecurityContextUtil.getCurrentUser().getId());
    }

    @PostMapping("/addresses")
    public ShippingAddressEntity addNewAddressEntity(@Valid @RequestBody ShippingAddressRequest request) {
        return this.userService.addNewShippingAddress(request);
    }

    @PatchMapping("/addresses/{id}")
    public ShippingAddressEntity updateShippingAddress(@PathVariable long id, @RequestBody ShippingAddressRequest request) {
        return this.userService.updateShippingAddress(SecurityContextUtil.getCurrentUser().getId(), id, request);
    }

    @DeleteMapping("/addresses/{id}")
    public void deleteShippingAddress(@PathVariable long id) {
        this.userService.deleteShippingAddress(SecurityContextUtil.getCurrentUser().getId(), id);
    }

    @GetMapping("/profile")
    public UserEntity getUserProfile() {
        return this.userService.getUserById(SecurityContextUtil.getCurrentUser().getId());
    }

    @PatchMapping("/profile")
    public UserEntity updateUserProfile(@Valid @RequestBody UserProfileRequest request) {
        return this.userService.updateUserProfile(SecurityContextUtil.getCurrentUser().getId(), request);
    }

    @GetMapping("/products/{productId}/favorite")
    public void addProduct2Wishlist(@PathVariable long productId) {
        this.userService.addProduct2Wishlist(SecurityContextUtil.getCurrentUser().getId(), productId);
    }

    @DeleteMapping("/products/{id}/favorite")
    public void removeWishlistItem(@PathVariable("id") long wishlistId) {
        this.userService.removeWishlistItem(SecurityContextUtil.getCurrentUser().getId(), wishlistId);
    }
}
