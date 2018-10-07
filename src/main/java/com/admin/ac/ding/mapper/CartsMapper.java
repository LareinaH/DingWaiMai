package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.Carts;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface CartsMapper extends BaseMapper<Carts> {
    @Update("update carts set is_deleted = 1 where user_id = #{userId} and is_deleted = 0")
    void delCartsForUser(
            @Param("userId") String userId
    );
}