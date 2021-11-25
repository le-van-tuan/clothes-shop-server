package vn.triumphstudio.clothesshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.triumphstudio.clothesshop.domain.entity.CategoryEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductImageEntity;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.repository.CategoryRepository;
import vn.triumphstudio.clothesshop.repository.ProductRepository;
import vn.triumphstudio.clothesshop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    @Override
    public CategoryEntity addNewCategory(CategoryRequest request) {
        CategoryEntity entity = new CategoryEntity();
        if (request.getParent() > 0) {
            Optional<CategoryEntity> parent = this.categoryRepository.findById(request.getParent());
            entity.setParentId(parent.map(CategoryEntity::getId).orElse(0L));
        }
        entity.setName(request.getName());
        entity.setImageUrl(request.getImage());
        entity.setEnabled(true);
        return this.categoryRepository.save(entity);
    }

    @Override
    public List<ProductEntity> getAllProduct() {
        return this.productRepository.findAll();
    }

    @Override
    public ProductEntity getProductById(long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new BusinessLogicException("No product found with id = " + productId));
    }

    @Override
    @Transactional
    public ProductEntity addNewProduct(ProductRequest request) {
        ProductEntity product = new ProductEntity();

        product.setName(request.getName());
        product.setCategory(this.categoryRepository.getOne(request.getCategory()));
        product.setPublished(request.isPublished());
        product.setDescription(request.getDescription());

        List<ProductImageEntity> productImages = new ArrayList<>();
        for (ProductRequest.ImageRequest requestImg : request.getImages()) {
            ProductImageEntity productImage = new ProductImageEntity();
            productImage.setProduct(product);
            productImage.setType(requestImg.getImageType());
            productImage.setUrl(requestImg.getName());
            productImages.add(productImage);
        }
        product.setImages(productImages);

        return this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        ProductEntity entity = this.getProductById(id);
        entity.setDeleted(true);
        this.productRepository.save(entity);
    }
}
