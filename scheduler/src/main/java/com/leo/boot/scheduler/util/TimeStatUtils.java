package com.leo.boot.scheduler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class TimeStatUtils {

    private static final Logger logger = LoggerFactory.getLogger(TimeStatUtils.class);

    private static final AtomicLong taskIdGenerator = new AtomicLong(1);

    public static Runnable decorate(Runnable runnable) {
        long taskId = taskIdGenerator.getAndIncrement();
        logger.info("task {} submit.", taskId);
        return () -> {
            long beginTime = System.currentTimeMillis();
            logger.info("task {} begin.", taskId);
            try {
                runnable.run();
            } catch (Exception e) {
                logger.error("task {} execute error.", taskId);
                throw e;
            } finally {
                logger.info("task {} end, spent {} ms.", taskId, System.currentTimeMillis() - beginTime);
            }
        };
    }
}
