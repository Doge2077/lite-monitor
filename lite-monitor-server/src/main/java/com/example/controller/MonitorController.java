package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.ClientSimpleVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Resource
    ClientService clientService;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllCLient() {
        return RestBean.success(this.clientService.listClients());
    }

    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList() {
        return RestBean.success(this.clientService.listSimpleList());
    }

    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO renameClientVO) {
        this.clientService.renameClient(renameClientVO);
        return RestBean.success();
    }

    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO renameNodeVO) {
        this.clientService.renameNode(renameNodeVO);
        return RestBean.success();
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId) {
        return RestBean.success(this.clientService.clientDetails(clientId));
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(int clientId) {
        return RestBean.success(this.clientService.clientRuntimeDetailsHistory(clientId));
    }

    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(int clientId) {
        return RestBean.success(this.clientService.clientRuntimeDetailsNow(clientId));
    }

    @GetMapping("/register")
    public RestBean<String> registerToken() {
        return RestBean.success(this.clientService.getRegisterToken());
    }

    @GetMapping("/delete")
    public RestBean<String> deleteClient(int clientId) {
        this.clientService.deleteClient(clientId);
        return RestBean.success();
    }

}
