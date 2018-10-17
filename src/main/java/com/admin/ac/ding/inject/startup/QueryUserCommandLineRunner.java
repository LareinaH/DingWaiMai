package com.admin.ac.ding.inject.startup;

import com.admin.ac.ding.mapper.OrderMapper;
import com.admin.ac.ding.model.Order;
import com.admin.ac.ding.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QueryUserCommandLineRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(QueryUserCommandLineRunner.class);

    @Autowired
    CacheService cacheService;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public void run(String... strings) throws Exception {
        orderMapper.select(new Order()).stream().forEach(x -> {
            try {
                cacheService.getUserDetail(x.getUserId());
            } catch (Exception e) {

            }
        });

        logger.info("cache stat:{}", cacheService.getCacheStat());
        logger.info("cache keys:{}", cacheService.getCacheKeys());
    }
}
