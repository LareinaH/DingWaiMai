package com.admin.ac.ding.model;

import java.util.List;

public class OrderDetailVO extends Order {
    List<OrderDetail> orderDetailList;
    OapiUserGetWithDeptResponse userDetail;

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public OapiUserGetWithDeptResponse getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(OapiUserGetWithDeptResponse userDetail) {
        this.userDetail = userDetail;
    }
}
