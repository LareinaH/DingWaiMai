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
            "b.commodity_id," +
            "b.commodity_name," +
            "b.commodity_spec," +
            "a.order_time_type," +
            "sum( b.commodity_amount ) AS total " +
            "FROM " +
            "dingwaimai.order a," +
            "dingwaimai.order_detail b " +
            "WHERE " +
            "a.gmt_create >= #{gmtStart} " +
            "AND a.gmt_create <= #{gmtEnd} " +
            "AND a.order_status = 'BILLED' " +
            "AND a.is_deleted = 0 " +
            "AND b.is_deleted = 0 " +
            "AND a.order_id = b.order_id " +
            "GROUP BY " +
            "b.commodity_id")
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

    @Select("select" +
            " b.commodity_id as commodityId,sum(b.commodity_amount) as total" +
            " from dingwaimai.order a, dingwaimai.order_detail b" +
            " where a.is_deleted = 0  and a.gmt_create >= #{gmtStart}" +
            " and a.gmt_create <= #{gmtEnd}" +
            " and a.order_status = 'COMPLETED'" +
            " and a.order_id = b.order_id and b.is_deleted = 0" +
            " group by b.commodity_id")
    List<Map<String, Object>> getOrderCommoditySales(
            @Param("gmtStart") String gmtStart,
            @Param("gmtEnd") String gmtEnd
    );
}