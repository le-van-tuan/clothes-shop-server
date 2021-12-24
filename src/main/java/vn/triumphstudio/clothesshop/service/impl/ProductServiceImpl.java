package vn.triumphstudio.clothesshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.entity.*;
import vn.triumphstudio.clothesshop.domain.enumration.ImageType;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;
import vn.triumphstudio.clothesshop.domain.model.AttributesInfo;
import vn.triumphstudio.clothesshop.domain.model.OptionInfo;
import vn.triumphstudio.clothesshop.domain.model.TierVariation;
import vn.triumphstudio.clothesshop.domain.request.CategoryRequest;
import vn.triumphstudio.clothesshop.domain.request.ClientFileInfo;
import vn.triumphstudio.clothesshop.domain.request.ProductRequest;
import vn.triumphstudio.clothesshop.domain.request.VariantRequest;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;
import vn.triumphstudio.clothesshop.domain.response.ProductDetail;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.repository.*;
import vn.triumphstudio.clothesshop.service.FileStorageService;
import vn.triumphstudio.clothesshop.service.ProductService;
import vn.triumphstudio.clothesshop.specifications.ProductSpecification;

import java.util.*;
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

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

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
        try {
            CategoryEntity category = this.getCategoryById(id);
            this.categoryRepository.delete(category);
        } catch (Exception exception) {
            throw new BusinessLogicException("Can not delete this category");
        }
    }

    @Override
    public List<ProductDetail> getAllProduct() {
        List<ProductEntity> productEntities = this.filterProducts(0, Integer.MAX_VALUE, null, null).getContent();
        return productEntities.stream().map(this::parseProduct2ProductDetails).collect(Collectors.toList());
    }

    @Override
    public ProductDetail getProductDetailsById(long id) {
        ProductEntity product = this.getProductById(id);
        return this.parseProduct2ProductDetails(product);
    }

    private ProductDetail parseProduct2ProductDetails(ProductEntity productEntity) {
        ProductDetail detail = new ProductDetail(productEntity);

        List<AttributeItem> specifications = new ArrayList<>();
        List<AttributeItem> specificationIds = new ArrayList<>();
        for (ProductAttributeEntity productAttribute : productEntity.getProductAttributes()) {
            specifications.add(new AttributeItem(productAttribute.getAttributeValue().getAttribute().getName(), productAttribute.getAttributeValue().getValue()));
            specificationIds.add(new AttributeItem(productAttribute.getAttributeValue().getAttribute().getId(), productAttribute.getAttributeValue().getId()));
        }
        detail.setSpecifications(specifications);
        detail.setSpecificationIds(specificationIds);

        List<ProductVariantEntity> variants = productEntity.getVariants()
                .stream()
                .filter(productVariantEntity -> !productVariantEntity.isDeleted())
                .sorted((Comparator.comparing(ProductVariantEntity::getCreatedAt)))
                .collect(Collectors.toList());
        detail.setVariants(variants);
        detail.setTierVariations(this.getListTierFromVariants(variants));

        return detail;
    }

    private List<TierVariation> getListTierFromVariants(List<ProductVariantEntity> variants) {
        List<ProductVariantOptionEntity> variantOptionEntities = new ArrayList<>();
        for (ProductVariantEntity variant : variants) {
            variantOptionEntities.addAll(variant.getVariantOptions());
        }

        Map<Long, List<TierVariation>> tierMap = new HashMap<>();
        for (ProductVariantOptionEntity variantOption : variantOptionEntities) {
            TierVariation tierVariation = new TierVariation();
            long key = variantOption.getAttributeValue().getAttribute().getId();
            tierVariation.setId(key);
            tierVariation.setName(variantOption.getAttributeValue().getAttribute().getName());
            tierVariation.setOptions(Collections.singletonList(new OptionInfo(variantOption.getAttributeValue().getId(), variantOption.getAttributeValue().getValue())));

            if (tierMap.containsKey(key)) {
                tierMap.get(key).add(tierVariation);
            } else {
                List<TierVariation> item = new ArrayList<>();
                item.add(tierVariation);
                tierMap.put(key, item);
            }
        }

        List<TierVariation> finalTier = new ArrayList<>();
        for (Map.Entry<Long, List<TierVariation>> longListEntry : tierMap.entrySet()) {
            TierVariation tierVariation = new TierVariation();
            tierVariation.setId(longListEntry.getKey());
            tierVariation.setName(longListEntry.getValue().get(0).getName());

            List<OptionInfo> list = new ArrayList<>();
            for (TierVariation variation : longListEntry.getValue()) {
                list.addAll(variation.getOptions());
            }
            tierVariation.setOptions(list);

            finalTier.add(tierVariation);
        }

        return finalTier;
    }

    @Override
    public Page<ProductEntity> filterProducts(int page, int size, List<Long> categories, Boolean published) {
        if (size <= 0) size = Integer.MAX_VALUE;
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        Specification<ProductEntity> productSpecs = ProductSpecification.byDeletedStatus(false)
                .and(ProductSpecification.byPublishedStatus(published))
                .and(ProductSpecification.byCategories(categories));

        return this.productRepository.findAll(productSpecs, pageable);
    }

    @Override
    public List<ProductEntity> getNewArrivals(int size) {
        if (size <= 0) size = 5;
        return this.filterProducts(0, size, null, true).getContent();
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
    @Transactional
    public ProductEntity updateProduct(long id, ProductRequest request) {
        ProductEntity existedProduct = this.getProductById(id);
        existedProduct.setName(request.getName());
        existedProduct.setCategory(this.getCategoryById(request.getCategory()));
        existedProduct.setPublished(existedProduct.isPublished());
        existedProduct.setDescription(request.getDescription());


        List<ProductAttributeEntity> attributeEntities = new ArrayList<>();
        for (AttributeItem specification : request.getSpecifications()) {
            ProductAttributeEntity attribute = new ProductAttributeEntity();
            String value = specification.getValue().toString();

            attribute.setProduct(existedProduct);
            attribute.setAttributeValue(this.attributeValueRepository.getOne(Long.valueOf(value)));
            attributeEntities.add(attribute);
        }
        existedProduct.getProductAttributes().clear();
        existedProduct.getProductAttributes().addAll(attributeEntities);

        if (request.getThumbnail() != null) {
            Optional<ProductImageEntity> optional = existedProduct.getImages().stream().filter(p -> p.getType().equals(ImageType.THUMBNAIL)).findFirst();
            if (optional.isPresent()) {
                ProductImageEntity existedThumb = this.productImageRepository.getOne(optional.get().getId());
                this.fileStorageService.deleteFile(optional.get().getUrl());
                this.productImageRepository.deleteById(existedThumb.getId());
            }

            FileUploadResponse uploaded = this.fileStorageService.uploadFile(request.getThumbnail());
            ProductImageEntity thumbnail = new ProductImageEntity();
            thumbnail.setProduct(existedProduct);
            thumbnail.setType(ImageType.THUMBNAIL);
            thumbnail.setUrl(uploaded.getFileName());
            this.productImageRepository.save(thumbnail);
        }

        if (request.isUpdateGalleries()) {
            if (!CollectionUtils.isEmpty(request.getDeletedGalleries())) {
                Set<Long> removedIds = new HashSet<>();
                for (ClientFileInfo clientFileInfo : request.getDeletedGalleries()) {
                    if (clientFileInfo.isExisted() && Objects.equals(clientFileInfo.getStatus(), "removed")) {
                        removedIds.add(clientFileInfo.getId());
                    }
                }
                List<ProductImageEntity> beingDelete = existedProduct.getImages().stream()
                        .filter(pImg -> pImg.getType().equals(ImageType.GALLERY) && removedIds.contains(pImg.getId())).collect(Collectors.toList());
                for (ProductImageEntity gallery : beingDelete) {
                    this.fileStorageService.deleteFile(gallery.getUrl());
                    this.productImageRepository.deleteById(gallery.getId());
                }
            }
            if (request.getGalleries() != null) {
                for (MultipartFile multipartFile : request.getGalleries()) {
                    FileUploadResponse uploaded = this.fileStorageService.uploadFile(multipartFile);
                    ProductImageEntity gallery = new ProductImageEntity();
                    gallery.setProduct(existedProduct);
                    gallery.setType(ImageType.GALLERY);
                    gallery.setUrl(uploaded.getFileName());
                    this.productImageRepository.save(gallery);
                }
            }
        }
        return existedProduct;
    }

    @Override
    public void publishProduct(long productId) {
        ProductEntity product = this.getProductById(productId);
        if (product.isPublished()) throw new BusinessLogicException("Product has already published!");
        if (product.isDeleted()) throw new BusinessLogicException("Product was deleted, it can not publish");
        List<ProductVariantEntity> variants = product.getVariants().stream().filter(productVariantEntity -> !productVariantEntity.isDeleted()).collect(Collectors.toList());
        if (variants.isEmpty())
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

    @Override
    @Transactional
    public ProductVariantEntity addProductVariant(VariantRequest request) {
        ProductEntity product = this.getProductById(request.getProductId());
        if (product == null) {
            throw new BusinessLogicException("Can not find any product with id = " + request.getProductId());
        }

        ProductVariantEntity variant = new ProductVariantEntity();
        variant.setProduct(product);
        variant.setVariantName(StringUtils.isEmpty(request.getName()) ? product.getName() : request.getName());
        variant.setCost(request.getCost());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        variant.setDeleted(false);

        List<ProductVariantImageEntity> galleries = new ArrayList<>();
        for (MultipartFile gallery : request.getGalleries()) {
            FileUploadResponse uploaded = this.fileStorageService.uploadFile(gallery);

            ProductVariantImageEntity variantImage = new ProductVariantImageEntity();
            variantImage.setProductVariant(variant);
            variantImage.setUrl(uploaded.getFileName());
            galleries.add(variantImage);
        }
        variant.setImages(galleries);

        List<ProductVariantOptionEntity> productVariantOptions = new ArrayList<>();
        for (AttributeItem attributeItem : request.getOptions()) {
            ProductVariantOptionEntity option = new ProductVariantOptionEntity();
            String value = attributeItem.getValue().toString();
            option.setAttributeValue(this.attributeValueRepository.getOne(Long.valueOf(value)));
            option.setProductVariant(variant);
            productVariantOptions.add(option);
        }
        variant.setVariantOptions(productVariantOptions);

        return this.productVariantRepository.save(variant);
    }

    @Override
    public void deleteProductVariants(long variantId) {
        ProductVariantEntity variant = this.productVariantRepository.getOne(variantId);
        variant.setDeleted(true);
        this.productVariantRepository.save(variant);
    }

    @Override
    public ProductVariantEntity getProductVariantById(long variantId) {
        return this.productVariantRepository.findById(variantId).orElseThrow(() -> new BusinessLogicException("No variant found with id = " + variantId));
    }
}
