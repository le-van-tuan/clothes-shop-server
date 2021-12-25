package vn.triumphstudio.clothesshop.domain.response;

import org.springframework.beans.BeanUtils;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;

import java.util.List;

public class ProductDetail extends ProductEntity {

    private List<AttributeItem> specifications;

    private List<AttributeItem> specificationIds;

    public ProductDetail(ProductEntity product) {
        BeanUtils.copyProperties(product, this);
    }

    public List<AttributeItem> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<AttributeItem> specifications) {
        this.specifications = specifications;
    }

    public List<AttributeItem> getSpecificationIds() {
        return specificationIds;
    }

    public void setSpecificationIds(List<AttributeItem> specificationIds) {
        this.specificationIds = specificationIds;
    }
}
