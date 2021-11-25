package vn.triumphstudio.clothesshop.domain.request;

import vn.triumphstudio.clothesshop.domain.enumration.ImageType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class ProductRequest {

    @NotBlank
    private String name;

    @Min(1)
    private long category;

    private String description;

    private List<ImageRequest> images;

    private boolean published;

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

    public List<ImageRequest> getImages() {
        return images;
    }

    public void setImages(List<ImageRequest> images) {
        this.images = images;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public static class ImageRequest {
        private String name;

        private ImageType imageType = ImageType.GALLERY;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ImageType getImageType() {
            return imageType;
        }

        public void setImageType(ImageType imageType) {
            this.imageType = imageType;
        }
    }
}
