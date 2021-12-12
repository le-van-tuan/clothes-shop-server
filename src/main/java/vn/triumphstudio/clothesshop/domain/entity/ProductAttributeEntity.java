package vn.triumphstudio.clothesshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "ProductAttributeEntity")
@Table(name = "product_attributes")
public class ProductAttributeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_value_id", nullable = false)
    private AttributeValueEntity attributeValue;

    @JsonIgnore
    @Column(name = "created_at")
    @CreationTimestamp
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

    public AttributeValueEntity getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(AttributeValueEntity attributeValue) {
        this.attributeValue = attributeValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
