package vn.triumphstudio.clothesshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "AttributeEntity")
@Table(name = "attributes")
public class AttributeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private List<AttributeValueEntity> values;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeValueEntity> getValues() {
        return values;
    }

    public void setValues(List<AttributeValueEntity> values) {
        this.values = values;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
