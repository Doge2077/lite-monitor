package org.example.aop;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.config.QuartzConfiguration;
import org.example.config.ServerConfiguration;
import org.example.entity.ConnectionConfig;
import org.example.utils.MonitorUtils;
import org.example.utils.NetUtils;
import org.quartz.Scheduler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitClientDetail implements ApplicationRunner {

    @Resource
    Scheduler scheduler;

    @Resource
    QuartzConfiguration quartzConfiguration;

    @Resource
    ServerConfiguration serverConfiguration;

    @Resource
    NetUtils netUtils;

    @Resource
    MonitorUtils monitorUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ConnectionConfig connectionConfig = this.serverConfiguration.connectionConfig();
        if (connectionConfig != null) {
            this.netUtils.updateClientDetail(this.monitorUtils.getClientDetail());
            // 创建定时任务
            this.scheduler.scheduleJob(this.quartzConfiguration.jobDetailFactoryBean(),
                    this.quartzConfiguration.cronTriggerFactoryBean());
        } else {
            log.error("初始化数据上报出错");
        }
    }
}
