package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT " +
            "c.commodity_id," +
            "c.commodity_name," +
            "c.commodity_spec," +
            "sum( b.commodity_amount ) AS total " +
            "FROM " +
            "dingwaimai.order a," +
            "dingwaimai.order_detail b," +
            "dingwaimai.commodity c " +
            "WHERE " +
            "a.gmt_create >= #{gmtStart} " +
            "AND a.gmt_create <= #{gmtEnd} " +
            "AND a.order_status = 'BILLED' " +
            "AND a.is_deleted = 0 " +
            "AND b.is_deleted = 0 " +
            "AND a.order_id = b.order_id " +
            "AND b.commodity_id = c.commodity_id " +
            "GROUP BY " +
            "c.commodity_id")
    List<Map<String, Object>> getOrderCommoditySummary(
            @Param("gmtStart") String gmtStart,
            @Param("gmtEnd") String gmtEnd
    );

    @Update("update dingwaimai.order set" +
            " order_status = #{targetStatus}" +
            " where order_status = #{srcStatus}" +
            " and is_deleted = 0" +
            " and gmt_create >= #{gmtStart} " +
            " and gmt_create <= #{gmtEnd} ")
    void setOrderStatus(
            @Param("srcStatus") String srcStatus,
            @Param("targetStatus") String targetStatus,
            @Param("gmtStart") String gmtStart,
            @Param("gmtEnd") String gmtEnd
    );
}