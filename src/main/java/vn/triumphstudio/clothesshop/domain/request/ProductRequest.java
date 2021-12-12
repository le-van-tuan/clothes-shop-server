package vn.triumphstudio.clothesshop.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {

    @NotBlank
    private String name;

    @Min(1)
    private long category;

    private String description;

    @JsonIgnore
    private MultipartFile thumbnail;

    @JsonIgnore
    private MultipartFile[] galleries;

    private List<AttributeItem> specifications;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MultipartFile[] getGalleries() {
        return galleries;
    }

    public void setGalleries(MultipartFile[] galleries) {
        this.galleries = galleries;
    }

    public List<AttributeItem> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<AttributeItem> specifications) {
        this.specifications = specifications;
    }
}
