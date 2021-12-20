package vn.triumphstudio.clothesshop.domain.response;

import org.springframework.beans.BeanUtils;
import vn.triumphstudio.clothesshop.domain.entity.ProductEntity;
import vn.triumphstudio.clothesshop.domain.model.AttributeItem;
import vn.triumphstudio.clothesshop.domain.model.TierVariation;

import java.util.List;

public class ProductDetail extends ProductEntity {

    private List<AttributeItem> specifications;

    private List<TierVariation> tierVariations;

    public ProductDetail(ProductEntity product) {
        BeanUtils.copyProperties(product, this);
    }

    public List<AttributeItem> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<AttributeItem> specifications) {
        this.specifications = specifications;
    }

    public List<TierVariation> getTierVariations() {
        return tierVariations;
    }

    public void setTierVariations(List<TierVariation> tierVariations) {
        this.tierVariations = tierVariations;
    }
}
