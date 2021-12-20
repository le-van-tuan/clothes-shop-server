package vn.triumphstudio.clothesshop.domain.model;

import java.util.List;

public class TierVariation {

    private long id;

    private String name;

    private List<OptionInfo> options;

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

    public List<OptionInfo> getOptions() {
        return options;
    }

    public void setOptions(List<OptionInfo> options) {
        this.options = options;
    }
}
