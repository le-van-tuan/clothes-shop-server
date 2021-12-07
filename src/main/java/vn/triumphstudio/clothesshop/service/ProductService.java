package vn.triumphstudio.clothesshop.service;

import vn.triumphstudio.clothesshop.domain.entity.CategoryEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;

import java.util.List;

public interface ProductService {

    List<CategoryEntity> getAllCategory();

    CategoryEntity getCategoryById(long id);

    CategoryEntity addNewCategory(CategoryRequest request);

    CategoryEntity updateCategory(long id, CategoryRequest request);

    void deleteCategory(long id);

    List<ProductEntity> getAllProduct();

    List<ProductEntity> getNewArrivals();

    ProductEntity getProductById(long productId);

    ProductEntity addNewProduct(ProductRequest request);

    void publishProduct(long productId);

    void deleteProduct(long id);
}
