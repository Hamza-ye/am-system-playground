package org.nmcpye.activitiesmanagement.extended.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Created by Hamza on 14/10/2021.
 */
@Configuration("coreServiceConfig")
public class ServiceConfig {

    @Bean("taskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(25);
        return threadPoolTaskScheduler;
    }
}
