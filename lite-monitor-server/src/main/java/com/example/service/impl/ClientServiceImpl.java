package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.entity.dto.ClientDetail;
import com.example.entity.dto.RuntimeDetail;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.mapper.ClientDetailMapper;
import com.example.mapper.ClientMapper;
import com.example.mapper.RuntimeDetailMapper;
import com.example.service.ClientService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Resource
    ClientDetailMapper clientDetailMapper;

    @Resource
    RuntimeDetailMapper runtimeDetailMapper;

    private String registerToken = this.generateNewToken();

    // 本地 Client Id 缓存
    private final Map<Integer, Client> clientIdCache = new ConcurrentHashMap<>();
    // 本地 Client Token 缓存
    private final Map<String, Client> clientTokenCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void initClientCache() {
        this.list().forEach(this::updateCache);
    }

    @Override
    public Boolean verifyAndRegister(String token) {
        if (this.registerToken.equals(token)) {
            int clientId = this.generateClientId();
            Client client = new Client(clientId, "未命名主机", token, new Date(), "未命名节点", "cn");
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
        return this.clientTokenCache.get(token);
    }

    @Override
    public Boolean updateClientDetail(ClientDetailVO clientDetailVO, Client client) {
        ClientDetail clientDetail = new ClientDetail();
        BeanUtils.copyProperties(clientDetailVO, clientDetail);
        clientDetail.setId(client.getId());
        if (clientDetailMapper.selectById(client.getId()) != null) {
            return clientDetailMapper.updateById(clientDetail) == 1;
        } else {
            return clientDetailMapper.insert(clientDetail) == 1;
        }
    }

    private Map<Integer, RuntimeDetailVO> currentRuntime = new ConcurrentHashMap<>();

    @Override
    public Boolean updateRuntimeDetail(RuntimeDetailVO runtimeDetailVO, Client client) {
        RuntimeDetail runtimeDetail = new RuntimeDetail();
        BeanUtils.copyProperties(runtimeDetailVO, runtimeDetail);
        runtimeDetail.setId(client.getId());
        currentRuntime.put(client.getId(), runtimeDetailVO);
        if (runtimeDetailMapper.selectById(client.getId()) != null) {
            return runtimeDetailMapper.updateById(runtimeDetail) == 1;
        } else {
            return runtimeDetailMapper.insert(runtimeDetail) == 1;
        }
    }

    private void updateCache(Client client) {
        this.clientIdCache.put(client.getId(), client);
        this.clientTokenCache.put(client.getClientToken(), client);
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
        log.info("生成 Token：{}", token);
        return token.toString();
    }

}
