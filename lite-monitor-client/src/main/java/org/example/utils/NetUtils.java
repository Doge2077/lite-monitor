package org.example.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ServerConfiguration;
import org.example.entity.ClientDetail;
import org.example.entity.ConnectionConfig;
import org.example.entity.Response;
import org.example.entity.RuntimeDetail;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class NetUtils {

    @Lazy
    @Resource
    ServerConfiguration serverConfiguration;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Boolean registerToServer(String serverAddress, String token) {
        log.info("正在注册到服务器");
        Response response = doGet("/register", serverAddress, token);
        if (response.success()) {
            log.info("客户端注册成功");
        } else {
            log.error("客户端注册失败：{}", response.msg());
        }
        return response.success();
    }

    private Response doGet(String url, String serverAddress, String token) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().GET()
                    .uri(new URI(serverAddress + "/monitor" + url))
                    .header("Authorization", token)
                    .build();
            HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(httpResponse.body()).to(Response.class);
        } catch (Exception e) {
            log.error("注册失败：{}", e.getMessage());
            return Response.errorResponse(e);
        }
    }

    public void updateClientDetail(ClientDetail clientDetail) {
        Response response = this.doPost("/detail", clientDetail);
        if (response.success()) {
            log.info("客户端数据上报完成");
        } else {
            log.error("客户端数据上报失败：{}", response.msg());
        }
    }

    public void updateRuntimeDetail(RuntimeDetail runtimeDetail) {
        Response response = this.doPost("/runtime", runtimeDetail);
        if (!response.success()) {
            log.error("客户端运行时数据上报失败：{}", response.msg());
        }
    }

    private Response doPost(String url, Object data) {
        try {
            String rawData = JSONObject.from(data).toJSONString();
            String token = this.serverConfiguration.getConfigurationFromFile().getToken();
            HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(rawData))
                    .uri(new URI(this.serverConfiguration.getConfigurationFromFile().getServerAddress() + "/monitor" + url))
                    .header("Authorization", this.serverConfiguration.getConfigurationFromFile().getToken())
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(httpResponse.body()).to(Response.class);
        } catch (Exception e) {
            log.error("客户端上报数据请求失败：{}", e.getMessage());
            return Response.errorResponse(e);
        }
    }

}
