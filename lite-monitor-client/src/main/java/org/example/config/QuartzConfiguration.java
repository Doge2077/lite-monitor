package org.example.config;

import org.example.task.MonitorJobBean;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    public JobDetail jobDetailFactoryBean() {
        return JobBuilder.newJob(MonitorJobBean.class)
                .withIdentity("monitor-runtime-task")
                .storeDurably().build();
    }

    public Trigger cronTriggerFactoryBean() throws InterruptedException {
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("*/1 * * * * ?");
        return TriggerBuilder.newTrigger()
                .withIdentity("monitor-runtime-trigger")
                .withSchedule(cron)
                .build();
    }
}
