package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.mapper.ClientMapper;
import com.example.service.ClientService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    private String registerToken = this.generateNewToken();

    // 本地 Client Id 缓存
    private final Map<Integer, Client> clientIdCache = new ConcurrentHashMap<>();
    // 本地 Client Token 缓存
    private final Map<String, Client> clientToeknCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void initClientCache() {
        this.list().forEach(this::updateCache);
    }

    @Override
    public Boolean verifyAndRegister(String token) {
        if (this.registerToken.equals(token)) {
            int clientId = this.generateClientId();
            Client client = new Client(clientId, "未命名主机", token, new Date());
            if (this.save(client)) {
                // 如果保存成功，则生成一个新的 registerToken 给下一个注册的 Client 使用
                this.registerToken = this.generateNewToken();
                // 更新本地缓存
                updateCache(client);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRegisterToken() {
        return this.registerToken;
    }

    @Override
    public Client getClientById(int id) {
        return this.clientIdCache.get(id);
    }

    @Override
    public Client getClientByToken(String token) {
        return this.clientToeknCache.get(token);
    }

    private void updateCache(Client client) {
        this.clientIdCache.put(client.getId(), client);
        this.clientToeknCache.put(client.getToken(), client);
    }

    private int generateClientId() {
        return new SecureRandom().nextInt(90000000) + 10000000;
    }

    private String generateNewToken() {
        String TEMPLATE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 24; i ++) {
            token.append(TEMPLATE.charAt(secureRandom.nextInt(TEMPLATE.length())));
        }
        log.info("生成 Token：{}", token.toString());
        return token.toString();
    }

}
