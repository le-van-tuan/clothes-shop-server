package vn.triumphstudio.clothesshop.service;

import vn.triumphstudio.clothesshop.domain.entity.CategoryEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;

import java.util.List;

public interface ProductService {

    List<CategoryEntity> getAllCategory();

    CategoryEntity addNewCategory(CategoryRequest request);

    List<ProductEntity> getAllProduct();

    ProductEntity getProductById(long productId);

    ProductEntity addNewProduct(ProductRequest request);

    void deleteProduct(long id);
}
