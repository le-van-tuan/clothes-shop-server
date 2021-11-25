package vn.triumphstudio.clothesshop.domain.request;

import javax.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank
    private String name;

    private String image;

    private long parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }
}
