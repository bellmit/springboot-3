package com.leo.boot.scheduler.executor;

import com.leo.boot.scheduler.util.MDCUtils;
import com.leo.boot.scheduler.util.TimeStatUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setKeepAliveSeconds(60);
        executor.setAllowCoreThreadTimeOut(false);
        executor.setTaskDecorator(taskDecorators());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskDecorators taskDecorators() {
        return new TaskDecorators()
                .add(MDCUtils::decorate)
                .add(TimeStatUtils::decorate);
    }
}
