package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.ClientSimpleVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.service.AccountService;
import com.example.service.ClientService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Resource
    AccountService accountService;

    @Resource
    ClientService clientService;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient(@RequestAttribute(Const.ATTR_USER_ID) int uid,
                                                         @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (isAdminAccount(role)) {
            return RestBean.success(this.clientService.listClients());
        }
        List<ClientPreviewVO> list = accountAccessList(uid).stream().map(clientId -> this.clientService.getClientById(clientId).asViewObject(ClientPreviewVO.class)
        ).toList();
        return RestBean.success(list);
    }

    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList(@RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (isAdminAccount(role)) {
            return RestBean.success(this.clientService.listSimpleList());
        }
        return RestBean.noPermission();
    }

    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO renameClientVO,
                                       @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                       @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (permissionCheck(uid, role, renameClientVO.getClientId())) {
            this.clientService.renameClient(renameClientVO);
            return RestBean.success();
        }
        return RestBean.noPermission();
    }

    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO renameNodeVO,
                                     @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                     @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (permissionCheck(uid, role, renameNodeVO.getClientId())) {
            this.clientService.renameNode(renameNodeVO);
            return RestBean.success();
        }
        return RestBean.noPermission();
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.clientDetails(clientId));
        }
        return RestBean.noPermission();
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(int clientId,
                                                            @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                                            @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.clientRuntimeDetailsHistory(clientId));
        }
        return RestBean.noPermission();
    }

    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(int clientId,
                                                       @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                                       @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.clientRuntimeDetailsNow(clientId));
        }
        return RestBean.noPermission();

    }

    @GetMapping("/register")
    public RestBean<String> registerToken(@RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (isAdminAccount(role)) {
            return RestBean.success(this.clientService.getRegisterToken());
        }
        return RestBean.noPermission();
    }

    @GetMapping("/delete")
    public RestBean<String> deleteClient(int clientId,
                                         @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (isAdminAccount(role)) {
            this.clientService.deleteClient(clientId);
            return RestBean.success();
        }
        return RestBean.noPermission();

    }

    private List<Integer> accountAccessList(int uid) {
        Account account = this.accountService.getById(uid);
        return account.getClientList();
    }

    private Boolean isAdminAccount(String role) {
        role = role.substring(5);
        return Const.ROLE_ADMIN.equals(role);
    }

    private Boolean permissionCheck(int uid, String role, int clientId) {
        if (isAdminAccount(role)) return true;
        return this.accountAccessList(uid).contains(clientId);
    }

}
