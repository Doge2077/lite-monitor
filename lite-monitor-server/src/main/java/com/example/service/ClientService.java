package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;

public interface ClientService extends IService<Client> {
    Boolean verifyAndRegister(String token);
    String getRegisterToken();
    Client getClientById(int id);
    Client getClientByToken(String token);
}
