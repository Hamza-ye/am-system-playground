package org.nmcpye.activitiesmanagement.extended.config;

import org.nmcpye.activitiesmanagement.extended.scheduling.JobConfigurationService;
import org.nmcpye.activitiesmanagement.extended.scheduling.SchedulingManager;
import org.nmcpye.activitiesmanagement.extended.system.SchedulerStart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

//    @Bean
//    public PeriodTypePopulator periodTypePopulator(PeriodTypeRepository periodTypeRepository) {
//        PeriodTypePopulator populator = new PeriodTypePopulator(periodTypeRepository);
//        populator.setName("PeriodTypePopulator");
//        populator.setRunlevel(3);
//        return populator;
//    }

    @Bean
    public SchedulerStart schedulerStart(JobConfigurationService jobConfigurationService, SchedulingManager schedulingManager) {
        SchedulerStart schedulerStart = new SchedulerStart("false", "2", jobConfigurationService, schedulingManager);
        schedulerStart.setRunlevel(14);
        schedulerStart.setSkipInTests(true);
        return schedulerStart;
    }
}
