package com.admin.ac.ding.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "`order_detail`")
public class OrderDetail extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.order_id
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private String orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.commodity_id
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private String commodityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.commodity_amount
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private Integer commodityAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.commodity_price
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private BigDecimal commodityPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.commodity_spec
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private String commoditySpec;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.commodity_name
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private String commodityName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_detail.commodity_pics
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    private String commodityPics;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.order_id
     *
     * @return the value of order_detail.order_id
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.order_id
     *
     * @param orderId the value for order_detail.order_id
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.commodity_id
     *
     * @return the value of order_detail.commodity_id
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.commodity_id
     *
     * @param commodityId the value for order_detail.commodity_id
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.commodity_amount
     *
     * @return the value of order_detail.commodity_amount
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public Integer getCommodityAmount() {
        return commodityAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.commodity_amount
     *
     * @param commodityAmount the value for order_detail.commodity_amount
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setCommodityAmount(Integer commodityAmount) {
        this.commodityAmount = commodityAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.commodity_price
     *
     * @return the value of order_detail.commodity_price
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.commodity_price
     *
     * @param commodityPrice the value for order_detail.commodity_price
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.commodity_spec
     *
     * @return the value of order_detail.commodity_spec
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public String getCommoditySpec() {
        return commoditySpec;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.commodity_spec
     *
     * @param commoditySpec the value for order_detail.commodity_spec
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setCommoditySpec(String commoditySpec) {
        this.commoditySpec = commoditySpec == null ? null : commoditySpec.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.commodity_name
     *
     * @return the value of order_detail.commodity_name
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public String getCommodityName() {
        return commodityName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.commodity_name
     *
     * @param commodityName the value for order_detail.commodity_name
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_detail.commodity_pics
     *
     * @return the value of order_detail.commodity_pics
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public String getCommodityPics() {
        return commodityPics;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_detail.commodity_pics
     *
     * @param commodityPics the value for order_detail.commodity_pics
     *
     * @mbg.generated Wed Oct 17 23:33:09 CST 2018
     */
    public void setCommodityPics(String commodityPics) {
        this.commodityPics = commodityPics == null ? null : commodityPics.trim();
    }
}