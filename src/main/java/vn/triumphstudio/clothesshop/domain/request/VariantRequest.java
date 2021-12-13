package vn.triumphstudio.clothesshop.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantRequest {

    private long productId;

    private String name;

    private double cost;

    private double price;

    private int stock;

    @JsonIgnore
    private MultipartFile[] galleries;

    private List<AttributeItem> options;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public MultipartFile[] getGalleries() {
        return galleries;
    }

    public void setGalleries(MultipartFile[] galleries) {
        this.galleries = galleries;
    }

    public List<AttributeItem> getOptions() {
        return options;
    }

    public void setOptions(List<AttributeItem> options) {
        this.options = options;
    }
}
