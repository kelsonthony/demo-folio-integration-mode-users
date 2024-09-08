package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.config;

import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.listener.StepSkipListener;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BatchInfrastructureConfig {

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(8);
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setQueueCapacity(8);

        return threadPoolTaskExecutor;
    }

    @Bean
    public SkipPolicy skipPolicy() {
        return (t, skipCount) -> !(t instanceof NonSkippableReadException);
    }

    @Bean
    public StepSkipListener skipListener() {
        return new StepSkipListener();
    }


}