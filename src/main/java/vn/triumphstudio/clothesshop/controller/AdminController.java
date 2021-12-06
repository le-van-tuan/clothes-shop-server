package vn.triumphstudio.clothesshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.annotation.AllowAdmin;
import vn.triumphstudio.clothesshop.domain.entity.CategoryEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;
import vn.triumphstudio.clothesshop.service.FileStorageService;
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
    @PostMapping(value = "/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ProductEntity addNewProduct(@RequestParam("product") String product, @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam(value = "galleries", required = false) MultipartFile[] galleries) throws IOException {
        ProductRequest request = this.objectMapper.readValue(product, ProductRequest.class);
        request.setThumbnail(thumbnail);
        request.setGalleries(galleries);
        return this.productService.addNewProduct(request);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable long id) {
        this.productService.deleteProduct(id);
    }

    @PostMapping("/file-management/images/upload")
    public FileUploadResponse uploadProductImage(@RequestParam("file") MultipartFile file) {
        return fileStorageService.uploadFile(file);
    }
}
