package com.admin.ac.ding.schedule;

import com.admin.ac.ding.controller.AdminController;
import com.admin.ac.ding.controller.DingController;
import com.admin.ac.ding.enums.OrderStatus;
import com.admin.ac.ding.mapper.OrderMapper;
import com.admin.ac.ding.service.DingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

@Component
public class ScheduledTask {
    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    DingService dingService;

    @Autowired
    AdminController adminController;

    @Autowired
    DingController dingController;

    @Autowired
    OrderMapper orderMapper;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;

    @Value("${ding.app.repair.agentid}")
    Long repairAppAgentId;

    @Value("${ding.app.suggest.agentid}")
    Long suggestAppAgentId;

    @Scheduled(cron = "0 0 10 * * ?" )
    public void setOrderStatusToBilled() {
        LocalDateTime now = LocalDateTime.now();
        // 昨天0点
        LocalDateTime lastDayZero = now.minusDays(1)
                .with(ChronoField.HOUR_OF_DAY, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MILLI_OF_SECOND, 0);

        // 昨天最后一秒
        LocalDateTime lastDayEnd = now.minusDays(1)
                .with(ChronoField.HOUR_OF_DAY, 23)
                .with(ChronoField.MINUTE_OF_HOUR, 59)
                .with(ChronoField.SECOND_OF_MINUTE, 59)
                .with(ChronoField.MILLI_OF_SECOND, 999);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        orderMapper.setOrderStatus(
                OrderStatus.SUBMITTED.name(),
                OrderStatus.BILLED.name(),
                df.format(lastDayZero),
                df.format(lastDayEnd)
        );
    }

    @Scheduled(cron = "0 0 11 * * ?" )
    public void setOrderStatusToCompleted() {
        LocalDateTime now = LocalDateTime.now();
        // 昨天0点
        LocalDateTime lastDayZero = now.minusDays(1)
                .with(ChronoField.HOUR_OF_DAY, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MILLI_OF_SECOND, 0);

        // 昨天最后一秒
        LocalDateTime lastDayEnd = now.minusDays(1)
                .with(ChronoField.HOUR_OF_DAY, 23)
                .with(ChronoField.MINUTE_OF_HOUR, 59)
                .with(ChronoField.SECOND_OF_MINUTE, 59)
                .with(ChronoField.MILLI_OF_SECOND, 999);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        orderMapper.setOrderStatus(
                OrderStatus.BILLED.name(),
                OrderStatus.COMPLETED.name(),
                df.format(lastDayZero),
                df.format(lastDayEnd)
        );
    }
}
