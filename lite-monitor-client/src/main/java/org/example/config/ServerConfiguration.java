package org.example.config;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.ConnectionConfig;
import org.example.utils.NetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
@Configuration
public class ServerConfiguration {

    @Resource
    NetUtils netUtils;

    @Bean
    public ConnectionConfig connectionConfig() {
        log.info("正在加载服务端连接配置 ...");
        ConnectionConfig connectionConfig = this.getConfigurationFromFile();
        if (connectionConfig == null) {
            connectionConfig = this.getConfigurationByRegister();
        }
        return connectionConfig;
    }

    private ConnectionConfig getConfigurationByRegister() {
        Scanner scanner = new Scanner(System.in);
        String serverAddress, token;
        do {
            log.info("请输入服务端地址及其端口号（127.0.0.1:8080）：");
            serverAddress = "http://" +  scanner.nextLine();
            log.info("请输入服务端生成的注册 Token 密钥：");
            token = scanner.nextLine();
        } while (!this.netUtils.registerToServer(serverAddress, token));
        ConnectionConfig connectionConfig = new ConnectionConfig(serverAddress, token);
        this.saveConnectionConfigAsFile(connectionConfig);
        return connectionConfig;
    }

    private void saveConnectionConfigAsFile(ConnectionConfig connectionConfig) {
        File configDir = new File("local-config");
        if (!configDir.exists() && configDir.mkdir()) {
            log.info("已创建 local-config 目录");
        }
        File configFile = new File("local-config/server.json");
        try (FileWriter fileWriter = new FileWriter(configFile)){
            fileWriter.write(JSONObject.from(connectionConfig).toJSONString());
            log.info("服务端连接配置已保存");
        } catch (Exception e) {
            log.error("服务端连接配置保存失败：{}", e.getMessage());
        }
    }

    public ConnectionConfig getConfigurationFromFile() {
        File config = new File("local-config/server.json");
        if (config.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(config)){
                String rawConfig = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
                ConnectionConfig connectionConfig = JSONObject.parseObject(rawConfig).to(ConnectionConfig.class);
                if (connectionConfig == null) {
                    log.error("配置文件为空");
                } else {
                    return connectionConfig;
                }
            } catch (IOException e) {
                log.error("读取配置文件错误：{}", e.getMessage());
            }
        }
        return null;
    }
}
