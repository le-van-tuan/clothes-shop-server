package vn.triumphstudio.clothesshop.service;

import vn.triumphstudio.clothesshop.domain.annotation.AllowAdmin;
import vn.triumphstudio.clothesshop.domain.entity.ShippingAddressEntity;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.domain.request.ShippingAddressRequest;
import vn.triumphstudio.clothesshop.domain.request.SignUpRequest;
import vn.triumphstudio.clothesshop.domain.request.UserProfileRequest;

import java.util.List;

public interface UserService {
    UserEntity getUserByEmail(String email);

    UserEntity getUserById(long id);

    @AllowAdmin
    UserEntity toggleUserStatus(long userId);

    UserEntity signUp(SignUpRequest signUpRequest);

    UserEntity updateUserProfile(long userId, UserProfileRequest request);

    List<ShippingAddressEntity> getShippingAddressesByUserId(long userId);

    ShippingAddressEntity addNewShippingAddress(ShippingAddressRequest request);

    ShippingAddressEntity updateShippingAddress(long userId, long addressId, ShippingAddressRequest request);

    void deleteShippingAddress(long userId, long addressId);

    /**
     * ADMINS
     */
    List<UserEntity> getAllUsers();
}
