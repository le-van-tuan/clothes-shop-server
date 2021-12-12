package vn.triumphstudio.clothesshop.domain.model;

import vn.triumphstudio.clothesshop.domain.entity.AttributeEntity;
import vn.triumphstudio.clothesshop.domain.entity.AttributeValueEntity;

import java.util.List;
import java.util.Map;

public class AttributesInfo {

    private List<AttributeEntity> attributes;

    private List<AttributeValueEntity> values;

    private Map<Long, List<AttributeValueEntity>> attributeValues;

    public List<AttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public List<AttributeValueEntity> getValues() {
        return values;
    }

    public void setValues(List<AttributeValueEntity> values) {
        this.values = values;
    }

    public Map<Long, List<AttributeValueEntity>> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(Map<Long, List<AttributeValueEntity>> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
