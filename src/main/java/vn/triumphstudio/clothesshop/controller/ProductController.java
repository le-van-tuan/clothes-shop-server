package vn.triumphstudio.clothesshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.triumphstudio.clothesshop.domain.entity.CategoryEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.response.ProductDetail;
import vn.triumphstudio.clothesshop.service.FileStorageService;
import vn.triumphstudio.clothesshop.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ProductDetail getProductDetails(@PathVariable long id) {
        return this.productService.getProductDetailsById(id);
    }

    @GetMapping("/new-arrivals")
    public List<ProductEntity> getNewArrivals(@RequestParam(value = "size") int size) {
        return this.productService.getNewArrivals(size);
    }

    @GetMapping("/categories")
    public List<CategoryEntity> getAllCategory() {
        return this.productService.getAllCategory();
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileStorageService.loadFile(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/filters")
    public Page<ProductEntity> filterProduct(@RequestParam(value = "categories", required = false) String categories) {
        List<Long> categoriesIds = new ArrayList<>();
        if (!StringUtils.isEmpty(categories)) {
            for (String s : categories.split(",")) {
                categoriesIds.add(Long.valueOf(s));
            }
        }
        return this.productService.filterProducts(categoriesIds);
    }
}
