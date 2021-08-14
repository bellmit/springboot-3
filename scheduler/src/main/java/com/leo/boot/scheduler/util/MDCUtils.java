package com.leo.boot.scheduler.util;

import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class MDCUtils {

    public static Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (!CollectionUtils.isEmpty(contextMap)) {
                MDC.setContextMap(contextMap);
            }
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
