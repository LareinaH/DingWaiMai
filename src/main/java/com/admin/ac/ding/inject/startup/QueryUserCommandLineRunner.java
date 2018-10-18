package com.admin.ac.ding.inject.startup;

import com.admin.ac.ding.mapper.OrderMapper;
import com.admin.ac.ding.mapper.SysRoleMapper;
import com.admin.ac.ding.model.Order;
import com.admin.ac.ding.model.SysRole;
import com.admin.ac.ding.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Component
public class QueryUserCommandLineRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(QueryUserCommandLineRunner.class);

    @Autowired
    CacheService cacheService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Override
    public void run(String... strings) throws Exception {
        orderMapper.select(new Order()).stream().forEach(x -> {
            try {
                cacheService.getUserDetail(x.getUserId());
            } catch (Exception e) {

            }
        });

        List<SysRole> sysRoleList = sysRoleMapper.select(new SysRole());
        for (SysRole sysRole : sysRoleList) {
            try {
                cacheService.getUserDetail(sysRole.getUserId());
            } catch (Exception e) {

            }
        }

        logger.info("cache stat:{}", cacheService.getCacheStat());
        logger.info("cache keys:{}", cacheService.getCacheKeys());
    }
}
