package vn.triumphstudio.clothesshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.entity.*;
import vn.triumphstudio.clothesshop.domain.enumration.ImageType;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;
import vn.triumphstudio.clothesshop.domain.model.AttributesInfo;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;
import vn.triumphstudio.clothesshop.domain.response.ProductDetail;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.repository.AttributeRepository;
import vn.triumphstudio.clothesshop.repository.AttributeValueRepository;
import vn.triumphstudio.clothesshop.repository.CategoryRepository;
import vn.triumphstudio.clothesshop.repository.ProductRepository;
import vn.triumphstudio.clothesshop.service.FileStorageService;
import vn.triumphstudio.clothesshop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

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
    public List<ProductDetail> getAllProduct() {
        List<ProductDetail> result = new ArrayList<>();
        List<ProductEntity> productEntities = this.productRepository.findAll();

        for (ProductEntity productEntity : productEntities) {
            ProductDetail detail = new ProductDetail(productEntity);
            List<AttributeItem> attributes = new ArrayList<>();
            for (ProductAttributeEntity productAttribute : productEntity.getProductAttributes()) {
                attributes.add(new AttributeItem(productAttribute.getAttributeValue().getAttribute().getName(), productAttribute.getAttributeValue().getValue()));
            }
            detail.setSpecifications(attributes);
            result.add(detail);
        }
        return result;
    }

    @Override
    public List<ProductEntity> getNewArrivals() {
        Pageable sortedByTime = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        return this.productRepository.findAll(sortedByTime).getContent();
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
        product.setPublished(false);
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

        List<ProductAttributeEntity> attributeEntities = new ArrayList<>();
        for (AttributeItem specification : request.getSpecifications()) {
            ProductAttributeEntity attribute = new ProductAttributeEntity();
            attribute.setProduct(product);
            String value = specification.getValue().toString();
            attribute.setAttributeValue(this.attributeValueRepository.getOne(Long.valueOf(value)));
            attributeEntities.add(attribute);
        }
        product.setProductAttributes(attributeEntities);
        product.setImages(productImages);
        return this.productRepository.save(product);
    }

    @Override
    public void publishProduct(long productId) {
        ProductEntity product = this.getProductById(productId);
        if (product.isPublished()) throw new BusinessLogicException("Product has already published!");
        if (product.isDeleted()) throw new BusinessLogicException("Product was deleted, it can not publish");
        if (product.getVariants().isEmpty())
            throw new BusinessLogicException("Product must has one variant to publish");
        product.setPublished(true);
        this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        ProductEntity entity = this.getProductById(id);
        entity.setDeleted(true);
        this.productRepository.save(entity);
    }

    @Override
    public AttributeEntity createAttribute(AttributeEntity attribute) {
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setName(attribute.getName());
        return this.attributeRepository.save(attributeEntity);
    }

    @Override
    public AttributeValueEntity createAttributeValue(long attributeId, AttributeValueEntity value) {
        AttributeEntity attribute = this.attributeRepository.getOne(attributeId);

        AttributeValueEntity attributeValue = new AttributeValueEntity();
        attributeValue.setAttribute(attribute);
        attributeValue.setValue(value.getValue());
        return this.attributeValueRepository.save(attributeValue);
    }

    @Override
    public AttributesInfo getAllAttributes() {
        AttributesInfo attributesInfo = new AttributesInfo();
        attributesInfo.setAttributes(this.attributeRepository.findAll());
        attributesInfo.setValues(this.attributeValueRepository.findAll());
        attributesInfo.setAttributeValues(this.attributeRepository.findAll().stream().collect(Collectors.toMap(AttributeEntity::getId, AttributeEntity::getValues)));

        return attributesInfo;
    }
}
