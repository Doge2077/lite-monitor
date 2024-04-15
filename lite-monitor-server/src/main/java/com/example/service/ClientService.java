package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.dto.RuntimeDetail;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.RuntimeHistoryVO;

import java.util.List;

public interface ClientService extends IService<Client> {
    Boolean verifyAndRegister(String token);
    String getRegisterToken();
    Client getClientById(int id);
    Client getClientByToken(String token);
    // 客户端数据上报
    Boolean updateClientDetail(ClientDetailVO clientDetailVO, Client client);
    // 客户端运行时数据上报
    Boolean updateRuntimeDetail(RuntimeDetailVO runtimeDetailVO, Client client);
    // 列出所有主机
    List<ClientPreviewVO> listClients();
    void renameClient(RenameClientVO renameClientVO);
    void renameNode(RenameNodeVO renameNodeVO);
    ClientDetailsVO clientDetails(int clientId);
    // 历史记录
    RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId);
    // 实时数据监控
    RuntimeDetailVO clientRuntimeDetailsNow(int clientId);
}
