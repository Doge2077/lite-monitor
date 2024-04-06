package org.example.config;

import org.example.task.MonitorJobBean;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail jobDetailFactoryBean() {
        return JobBuilder.newJob(MonitorJobBean.class)
                .withIdentity("monitor-runtime-task")
                .storeDurably().build();
    }

    @Bean
    public Trigger cronTriggerFactoryBean(JobDetail jobDetail) {
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("*/1 * * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("monitor-runtime-trigger")
                .withSchedule(cron)
                .build();
    }
}
