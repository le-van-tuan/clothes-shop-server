package vn.triumphstudio.clothesshop.domain.response;

import org.springframework.beans.BeanUtils;
import vn.triumphstudio.clothesshop.domain.entity.ProductVariantEntity;

public class ProductVariantDetail extends ProductVariantEntity {

    public ProductVariantDetail(ProductVariantEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
