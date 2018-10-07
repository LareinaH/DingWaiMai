package com.admin.ac.ding.controller;

import com.admin.ac.ding.service.CacheService;
import com.admin.ac.ding.service.DingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin", produces = "application/json; charset=UTF-8")
public class AdminController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    DingService dingService;

    @Autowired
    CacheService cacheService;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;
}
