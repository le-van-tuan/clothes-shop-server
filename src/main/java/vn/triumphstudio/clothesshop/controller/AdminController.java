package vn.triumphstudio.clothesshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.annotation.AllowAdmin;
import vn.triumphstudio.clothesshop.domain.entity.*;
import vn.triumphstudio.clothesshop.domain.enumration.OrderStatus;
import vn.triumphstudio.clothesshop.domain.model.AttributesInfo;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.request.VariantRequest;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;
import vn.triumphstudio.clothesshop.domain.response.ProductDetail;
import vn.triumphstudio.clothesshop.service.FileStorageService;
import vn.triumphstudio.clothesshop.service.OrderService;
import vn.triumphstudio.clothesshop.service.ProductService;
import vn.triumphstudio.clothesshop.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllowAdmin
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    /**
     * USERS
     *
     * @return
     */
    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/users/{id}/toggle-status")
    public UserEntity updateUser(@PathVariable long id) {
        return this.userService.toggleUserStatus(id);
    }

    /**
     * CATEGORIES
     *
     * @return
     */
    @PostMapping("/categories")
    public CategoryEntity addCategory(@Valid @RequestBody CategoryRequest request) {
        return this.productService.addNewCategory(request);
    }

    @PatchMapping("/categories/{id}")
    public CategoryEntity updateCategory(@PathVariable long id, @Valid @RequestBody CategoryRequest request) {
        return this.productService.updateCategory(id, request);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable long id) {
        this.productService.deleteCategory(id);
    }

    /**
     * PRODUCTS
     *
     * @return
     */
    @GetMapping("/products")
    public List<ProductDetail> getAllProduct() {
        return this.productService.getAllProduct();
    }

    @PostMapping(value = "/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ProductEntity addNewProduct(@RequestParam("product") String product, @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam(value = "galleries", required = false) MultipartFile[] galleries) throws IOException {
        ProductRequest request = this.objectMapper.readValue(product, ProductRequest.class);
        request.setThumbnail(thumbnail);
        request.setGalleries(galleries);
        return this.productService.addNewProduct(request);
    }

    @PostMapping(value = "/products/variants", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ProductVariantEntity addProductVariant(@RequestParam("variant") String variant, @RequestParam(value = "galleries") MultipartFile[] galleries) throws IOException {
        VariantRequest variantRequest = this.objectMapper.readValue(variant, VariantRequest.class);
        variantRequest.setGalleries(galleries);
        return this.productService.addProductVariant(variantRequest);
    }

    @DeleteMapping("/products/variants/{id}")
    public void deleteProductVariant(@PathVariable("id") long variantId) {
        this.productService.deleteProductVariants(variantId);
    }

    @GetMapping(value = "/products/{id}/publish")
    public void publishProduct(@PathVariable long id) {
        this.productService.publishProduct(id);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable long id) {
        this.productService.deleteProduct(id);
    }

    @PostMapping("/file-management/images/upload")
    public FileUploadResponse uploadProductImage(@RequestParam("file") MultipartFile file) {
        return fileStorageService.uploadFile(file);
    }

    /**
     * Attributes
     */
    @GetMapping("/attributes")
    public AttributesInfo getAllAttributes() {
        return this.productService.getAllAttributes();
    }

    @PostMapping("/attributes")
    public AttributeEntity createAttribute(@RequestBody AttributeEntity request) {
        return this.productService.createAttribute(request);
    }

    @PostMapping("/attributes/values/{id}")
    public AttributeValueEntity createAttributeValue(@PathVariable("id") long attributeId, @RequestBody AttributeValueEntity value) {
        return this.productService.createAttributeValue(attributeId, value);
    }

    @GetMapping("/orders")
    public List<OrderEntity> getAllOrders() {
        return this.orderService.getAllOrders();
    }

    @PatchMapping("/orders/{id}/{status}")
    public void changeOrderStatus(@PathVariable long id, @PathVariable OrderStatus status) {
        this.orderService.changeOrderStatus(id, status);
    }
}
