package com.github.yingzhuo.playground.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.turbo.bean.scheduling.ScheduledWeekly;
import spring.turbo.util.DateUtils;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingTask {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ScheduledWeekly
    public void execute() {
        log.debug(DateUtils.format(new Date(), DATE_PATTERN));
    }

}
