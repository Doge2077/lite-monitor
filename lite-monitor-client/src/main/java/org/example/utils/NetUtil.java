package org.example.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.ConnectionConfig;
import org.example.entity.Response;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class NetUtil {

    @Lazy
    @Resource
    ConnectionConfig connectionConfig;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Boolean registerToServer(String serverAddress, String token) {
        log.info("正在注册到服务器");
        Response response = doGet("/register", serverAddress, token);
        if (response.success()) {
            log.info("客户端注册成功");
        } else {
            log.info("客户端注册失败：{}", response.msg());
        }
        return response.success();
    }

    private Response doGet(String url) {
        return this.doGet(url, this.connectionConfig.getServerAddress(), this.connectionConfig.getToken());
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

}
