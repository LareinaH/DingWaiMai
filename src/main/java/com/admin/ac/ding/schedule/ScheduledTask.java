package com.admin.ac.ding.schedule;

import com.admin.ac.ding.controller.AdminController;
import com.admin.ac.ding.controller.DingController;
import com.admin.ac.ding.service.DingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    DingService dingService;

    @Autowired
    AdminController adminController;

    @Autowired
    DingController dingController;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;

    @Value("${ding.app.repair.agentid}")
    Long repairAppAgentId;

    @Value("${ding.app.suggest.agentid}")
    Long suggestAppAgentId;
}
