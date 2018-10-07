package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.Commodity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface CommodityMapper extends BaseMapper<Commodity> {
    @Update("update commodity set is_deleted = 1 where is_deleted = 0 and id = #{id}")
    void delCommodityById(@Param("id") Long id);
}