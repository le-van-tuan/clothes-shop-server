package vn.triumphstudio.clothesshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "ProductVariantEntity")
@Table(name = "product_variants")
public class ProductVariantEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "variant_name")
    private String variantName;

    private double cost;

    private double price;

    private int stock;

    private boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_variant_id", referencedColumnName = "id")
    private List<ProductVariantImageEntity> images;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productVariant")
    private List<ProductVariantOptionEntity> variantOptions;

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm a")
    private LocalDateTime createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<ProductVariantImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ProductVariantImageEntity> images) {
        this.images = images;
    }

    public List<ProductVariantOptionEntity> getVariantOptions() {
        return variantOptions;
    }

    public void setVariantOptions(List<ProductVariantOptionEntity> variantOptions) {
        this.variantOptions = variantOptions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Transient
    public String getVariantString() {
        String result = "";
        for (int i = 0; i < this.variantOptions.size(); i++) {
            result = result.concat(this.variantOptions.get(i).getAttributeValue().getAttribute().getName() + ": " + this.variantOptions.get(i).getAttributeValue().getValue());
            if (this.variantOptions.size() >= 2 && i != this.variantOptions.size() - 1) {
                result = result.concat(", ");
            }
        }
        return result;
    }

    @Transient
    public List<AttributeItem> getOptionsIds() {
        List<AttributeItem> optionsIds = new ArrayList<>();
        for (ProductVariantOptionEntity variantOption : this.variantOptions) {
            optionsIds.add(new AttributeItem(variantOption.getAttributeValue().getAttribute().getId(), variantOption.getAttributeValue().getId()));
        }
        return optionsIds;
    }
}
