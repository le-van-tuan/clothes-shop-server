package vn.triumphstudio.clothesshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.entity.CategoryEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.entity.ProductImageEntity;
import vn.triumphstudio.clothesshop.domain.enumration.ImageType;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.repository.CategoryRepository;
import vn.triumphstudio.clothesshop.repository.ProductRepository;
import vn.triumphstudio.clothesshop.service.FileStorageService;
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

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public List<CategoryEntity> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    @Override
    public CategoryEntity getCategoryById(long id) {
        return this.categoryRepository.findById(id).orElseThrow(() -> new BusinessLogicException("No category found with id = " + id));
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
        entity.setEnabled(request.isEnabled());
        return this.categoryRepository.save(entity);
    }

    @Override
    public CategoryEntity updateCategory(long id, CategoryRequest request) {
        CategoryEntity category = this.getCategoryById(id);
        category.setEnabled(request.isEnabled());
        category.setName(request.getName());

        return this.categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(long id) {
        CategoryEntity category = this.getCategoryById(id);
        this.categoryRepository.delete(category);
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
        product.setCategory(this.getCategoryById(request.getCategory()));
        product.setPublished(request.isPublished());
        product.setDescription(request.getDescription());

        List<ProductImageEntity> productImages = new ArrayList<>();
        if (request.getThumbnail() != null) {
            FileUploadResponse uploaded = this.fileStorageService.uploadFile(request.getThumbnail());
            ProductImageEntity thumbnail = new ProductImageEntity();
            thumbnail.setProduct(product);
            thumbnail.setType(ImageType.THUMBNAIL);
            thumbnail.setUrl(uploaded.getFileName());
            productImages.add(thumbnail);
        }
        for (MultipartFile multipartFile : request.getGalleries()) {
            FileUploadResponse uploaded = this.fileStorageService.uploadFile(multipartFile);
            ProductImageEntity gallery = new ProductImageEntity();
            gallery.setProduct(product);
            gallery.setType(ImageType.GALLERY);
            gallery.setUrl(uploaded.getFileName());
            productImages.add(gallery);
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
