package com.admin.ac.ding.inject.startup;

import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.CacheService;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class QueryUserCommandLineRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(QueryUserCommandLineRunner.class);

    @Autowired
    CacheService cacheService;

    @Override
    public void run(String... strings) throws Exception {


        logger.info("cache stat:{}", cacheService.getCacheStat());
        logger.info("cache keys:{}", cacheService.getCacheKeys());
    }
}
