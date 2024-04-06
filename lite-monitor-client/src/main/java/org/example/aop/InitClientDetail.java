package org.example.aop;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ServerConfiguration;
import org.example.entity.ConnectionConfig;
import org.example.utils.MonitorUtils;
import org.example.utils.NetUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitClientDetail implements ApplicationRunner {

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
        } else {
            log.error("初始化数据上报出错");
        }
    }
}
