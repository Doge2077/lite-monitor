package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.entity.dto.ClientDetail;
import com.example.entity.dto.ClientSsh;
import com.example.entity.vo.request.*;
import com.example.entity.vo.response.*;
import com.example.mapper.ClientDetailMapper;
import com.example.mapper.ClientMapper;
import com.example.mapper.ClientSshMapper;
import com.example.service.ClientService;
import com.example.utils.InfluxDbUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Resource
    ClientDetailMapper clientDetailMapper;

    @Resource
    ClientSshMapper clientSshMapper;

    @Resource
    InfluxDbUtils influxDbUtils;

    private String registerToken = this.generateNewToken();

    // 本地 Client Id 缓存
    private final Map<Integer, Client> clientIdCache = new ConcurrentHashMap<>();
    // 本地 Client Token 缓存
    private final Map<String, Client> clientTokenCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void initClientCache() {
        this.clientIdCache.clear();
        this.clientTokenCache.clear();
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
        clientDetail.setClientId(client.getClientId());
        if (clientDetailMapper.selectById(client.getClientId()) != null) {
            return clientDetailMapper.updateById(clientDetail) == 1;
        } else {
            return clientDetailMapper.insert(clientDetail) == 1;
        }
    }

    private final Map<Integer, RuntimeDetailVO> currentRuntime = new ConcurrentHashMap<>();

    @Override
    public Boolean updateRuntimeDetail(RuntimeDetailVO runtimeDetailVO, Client client) {
        this.currentRuntime.put(client.getClientId(), runtimeDetailVO);
        this.influxDbUtils.updateRuntimeDetial(client.getClientId(), runtimeDetailVO);
        return true;
    }

    @Override
    public List<ClientPreviewVO> listClients() {
        return clientIdCache.values().stream().map(client -> {
            ClientPreviewVO clientPreviewVO = client.asViewObject(ClientPreviewVO.class);
            BeanUtils.copyProperties(this.clientDetailMapper.selectById(client.getClientId()), clientPreviewVO);
            RuntimeDetailVO runtimeDetailVO = this.currentRuntime.get(client.getClientId());
            if (this.isOnline(runtimeDetailVO)) {
                BeanUtils.copyProperties(runtimeDetailVO, clientPreviewVO);
                clientPreviewVO.setOnline(true);
            }
            return clientPreviewVO;
        }).toList();
    }

    @Override
    public List<ClientSimpleVO> listSimpleList() {
        return this.clientIdCache.values().stream().map(client -> {
            ClientSimpleVO clientSimpleVO = client.asViewObject(ClientSimpleVO.class);
            BeanUtils.copyProperties(this.clientDetailMapper.selectById(clientSimpleVO.getClientId()), clientSimpleVO);
            return clientSimpleVO;
        }).toList();
    }

    @Override
    public void renameClient(RenameClientVO renameClientVO) {
        this.update(Wrappers.<Client>update().eq("client_id", renameClientVO.getClientId()).set("client_name", renameClientVO.getClientName()));
        this.initClientCache();
    }

    @Override
    public void renameNode(RenameNodeVO renameNodeVO) {
        this.update(Wrappers.<Client>update().eq("client_id", renameNodeVO.getClientId()).set("node", renameNodeVO.getNode()).set("location",renameNodeVO.getLocation()));
        this.initClientCache();
    }

    @Override
    public ClientDetailsVO clientDetails(int clientId) {
        ClientDetailsVO clientDetailsVO = this.clientIdCache.get(clientId).asViewObject(ClientDetailsVO.class);
        BeanUtils.copyProperties(clientDetailMapper.selectById(clientId), clientDetailsVO);
        clientDetailsVO.setOnline(this.isOnline(currentRuntime.get(clientId)));
        return clientDetailsVO;
    }

    @Override
    public RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId) {
        RuntimeHistoryVO runtimeHistoryVO = this.influxDbUtils.readRuntimeData(clientId);
        ClientDetail clientDetail = this.clientDetailMapper.selectById(clientId);
        BeanUtils.copyProperties(clientDetail, runtimeHistoryVO);
        return runtimeHistoryVO;
    }

    @Override
    public RuntimeDetailVO clientRuntimeDetailsNow(int clientId) {
        return this.currentRuntime.get(clientId);
    }

    @Override
    public void deleteClient(int clientId) {
        this.removeById(clientId);
        clientDetailMapper.deleteById(clientId);
        this.initClientCache();
        this.currentRuntime.remove(clientId);
    }

    @Override
    public void saveClientSshConnection(SshConnectionVO sshConnectionVO) {
        Client client = this.clientIdCache.get(sshConnectionVO.getClientId());
        if (client == null) return ;
        ClientSsh clientSsh = new ClientSsh();
        BeanUtils.copyProperties(sshConnectionVO, clientSsh);
        if (Objects.nonNull(this.clientSshMapper.selectById(client.getClientId()))) {
            this.clientSshMapper.updateById(clientSsh);
        } else {
            this.clientSshMapper.insert(clientSsh);
        }
    }

    @Override
    public SshSettingsVO sshSettings(int clientId) {
        ClientDetail clientDetail = this.clientDetailMapper.selectById(clientId);
        ClientSsh clientSsh = this.clientSshMapper.selectById(clientId);
        if (clientSsh == null) {
            SshSettingsVO sshSettingsVO = new SshSettingsVO();
            sshSettingsVO.setClientAddress(clientDetail.getClientAddress());
            return sshSettingsVO;
        }
        SshSettingsVO sshSettingsVO = clientSsh.asViewObject(SshSettingsVO.class);
        sshSettingsVO.setClientAddress(clientDetail.getClientAddress());
        return sshSettingsVO;
    }

    private void updateCache(Client client) {
        this.clientIdCache.put(client.getClientId(), client);
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

    private Boolean isOnline(RuntimeDetailVO runtimeDetailVO) {
        return runtimeDetailVO != null && System.currentTimeMillis() - runtimeDetailVO.getTimestamp() < 30 * 1000;
    }

}
