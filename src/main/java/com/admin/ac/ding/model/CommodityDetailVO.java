package com.admin.ac.ding.model;

import java.util.List;

public class CommodityDetailVO {
    Category category;
    List<Commodity> commodityList;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Commodity> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<Commodity> commodityList) {
        this.commodityList = commodityList;
    }
}
