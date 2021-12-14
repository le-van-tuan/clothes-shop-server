package vn.triumphstudio.clothesshop.service;

import org.springframework.data.domain.Page;
import vn.triumphstudio.clothesshop.domain.entity.*;
import vn.triumphstudio.clothesshop.domain.model.AttributesInfo;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.domain.request.VariantRequest;
import vn.triumphstudio.clothesshop.domain.response.ProductDetail;

import java.util.List;

public interface ProductService {

    List<CategoryEntity> getAllCategory();

    CategoryEntity getCategoryById(long id);

    CategoryEntity addNewCategory(CategoryRequest request);

    CategoryEntity updateCategory(long id, CategoryRequest request);

    void deleteCategory(long id);

    List<ProductDetail> getAllProduct();

    ProductDetail getProductDetailsById(long id);

    Page<ProductEntity> filterProducts(int page, int size, List<Long> categories, Boolean published);

    List<ProductEntity> getNewArrivals(int size);

    ProductEntity getProductById(long productId);

    ProductEntity addNewProduct(ProductRequest request);

    void publishProduct(long productId);

    void deleteProduct(long id);

    AttributeEntity createAttribute(AttributeEntity attribute);

    AttributeValueEntity createAttributeValue(long attributeId, AttributeValueEntity value);

    AttributesInfo getAllAttributes();

    ProductVariantEntity addProductVariant(VariantRequest variantRequest);

    void deleteProductVariants(long variantId);
}
