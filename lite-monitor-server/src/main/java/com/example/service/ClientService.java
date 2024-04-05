package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;

public interface ClientService extends IService<Client> {
    Boolean verifyAndRegister(String token);
    String getRegisterToken();
    Client getClientById(int id);
    Client getClientByToken(String token);
    // 客户端数据上报
    Boolean updateClientDetail(ClientDetailVO clientDetailVO, Client client);
}
