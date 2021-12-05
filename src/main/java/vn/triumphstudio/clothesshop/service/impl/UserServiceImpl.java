package vn.triumphstudio.clothesshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.entity.ShippingAddressEntity;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.domain.entity.WishlistEntity;
import vn.triumphstudio.clothesshop.domain.enumration.Role;
import vn.triumphstudio.clothesshop.domain.request.ShippingAddressRequest;
import vn.triumphstudio.clothesshop.domain.request.SignUpRequest;
import vn.triumphstudio.clothesshop.domain.request.UserProfileRequest;
import vn.triumphstudio.clothesshop.exception.BadRequestException;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.repository.ProductRepository;
import vn.triumphstudio.clothesshop.repository.ShippingAddressRepository;
import vn.triumphstudio.clothesshop.repository.UserRepository;
import vn.triumphstudio.clothesshop.repository.WishlistRepository;
import vn.triumphstudio.clothesshop.service.ProductService;
import vn.triumphstudio.clothesshop.service.UserService;
import vn.triumphstudio.clothesshop.util.SecurityContextUtil;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductService productService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessLogicException("No user found with id = " + id));
    }

    @Override
    public UserEntity toggleUserStatus(long userId) {
        UserEntity user = this.getUserById(userId);
        user.setEnabled(!user.isEnabled());
        return this.userRepository.save(user);
    }

    @Override
    public UserEntity signUp(SignUpRequest signUpRequest) {
        if (this.userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("User with email " + signUpRequest.getEmail() + " has already existed!");
        }
        UserEntity user = new UserEntity();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setName(signUpRequest.getName());

        return this.userRepository.save(user);
    }

    @Override
    public UserEntity updateUserProfile(long userId, UserProfileRequest request) {
        UserEntity user = this.getUserById(userId);
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return this.userRepository.save(user);
    }

    @Override
    public List<ShippingAddressEntity> getShippingAddressesByUserId(long userId) {
        return this.shippingAddressRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public ShippingAddressEntity addNewShippingAddress(ShippingAddressRequest request) {
        long userId = SecurityContextUtil.getCurrentUser().getId();
        if (request.isDefaultAddress()) {
            this.shippingAddressRepository.markAsUnDefaultShippingAddress(userId);
        }
        ShippingAddressEntity shippingAddress = new ShippingAddressEntity();

        shippingAddress.setUser(this.getUserById(userId));
        shippingAddress.setName(request.getName());
        shippingAddress.setCity(request.getCity());
        shippingAddress.setDistrict(request.getDistrict());
        shippingAddress.setWard(request.getWard());
        shippingAddress.setAddress(request.getAddress());
        shippingAddress.setPhoneNumber(request.getPhoneNumber());
        shippingAddress.setDefault(request.isDefaultAddress());

        return this.shippingAddressRepository.save(shippingAddress);
    }

    @Override
    @Transactional
    public ShippingAddressEntity updateShippingAddress(long userId, long addressId, ShippingAddressRequest request) {
        ShippingAddressEntity shippingAddress = this.shippingAddressRepository.findFirstByUser_IdAndId(userId, addressId);
        if (shippingAddress == null) throw new BusinessLogicException("No shipping address found.");

        if (request.isDefaultAddress()) {
            this.shippingAddressRepository.markAsUnDefaultShippingAddress(userId);
        }

        shippingAddress.setName(request.getName());
        shippingAddress.setCity(request.getCity());
        shippingAddress.setDistrict(request.getDistrict());
        shippingAddress.setWard(request.getWard());
        shippingAddress.setAddress(request.getAddress());
        shippingAddress.setPhoneNumber(request.getPhoneNumber());
        shippingAddress.setDefault(request.isDefaultAddress());
        return this.shippingAddressRepository.save(shippingAddress);
    }

    @Override
    public void deleteShippingAddress(long userId, long addressId) {
        ShippingAddressEntity shippingAddress = this.shippingAddressRepository.findFirstByUser_IdAndId(userId, addressId);
        if (shippingAddress == null) throw new BusinessLogicException("No shipping address found.");
        this.shippingAddressRepository.delete(shippingAddress);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void addProduct2Wishlist(long userId, long productId) {
        ProductEntity product = this.productService.getProductById(productId);
        UserEntity user = this.getUserById(userId);

        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setProduct(product);
        wishlist.setUser(user);
        this.wishlistRepository.save(wishlist);
    }

    @Override
    public void removeWishlistItem(long userId, long wishlistId) {
        WishlistEntity entity = this.wishlistRepository.findFirstByUser_IdAndId(userId, wishlistId);
        this.wishlistRepository.delete(entity);
    }
}
