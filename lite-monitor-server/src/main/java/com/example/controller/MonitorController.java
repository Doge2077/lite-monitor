package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.request.SshConnectionVO;
import com.example.entity.vo.response.*;
import com.example.service.AccountService;
import com.example.service.ClientService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

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
        if (this.isAdminAccount(role)) {
            return RestBean.success(this.clientService.listClients());
        }
        List<ClientPreviewVO> list = accountAccessList(uid).stream().map(clientId -> {
            ClientDetailsVO clientDetailsVO = this.clientService.clientDetails(clientId);
            RuntimeDetailVO runtimeDetailVO = this.clientService.getRuntimeDetailByClientId(clientId);
            ClientPreviewVO clientPreviewVO = new ClientPreviewVO();
            BeanUtils.copyProperties(clientDetailsVO, clientPreviewVO);
            BeanUtils.copyProperties(runtimeDetailVO, clientPreviewVO);
            return clientPreviewVO;
        }).toList();
        return RestBean.success(list);
    }

    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList(@RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.isAdminAccount(role)) {
            return RestBean.success(this.clientService.listSimpleList());
        }
        return RestBean.noPermission();
    }

    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO renameClientVO,
                                       @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                       @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, renameClientVO.getClientId())) {
            this.clientService.renameClient(renameClientVO);
            return RestBean.success();
        }
        return RestBean.noPermission();
    }

    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO renameNodeVO,
                                     @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                     @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, renameNodeVO.getClientId())) {
            this.clientService.renameNode(renameNodeVO);
            return RestBean.success();
        }
        return RestBean.noPermission();
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.clientDetails(clientId));
        }
        return RestBean.noPermission();
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(int clientId,
                                                            @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                                            @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.clientRuntimeDetailsHistory(clientId));
        }
        return RestBean.noPermission();
    }

    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(int clientId,
                                                       @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                                       @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.clientRuntimeDetailsNow(clientId));
        }
        return RestBean.noPermission();

    }

    @GetMapping("/register")
    public RestBean<String> registerToken(@RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.isAdminAccount(role)) {
            return RestBean.success(this.clientService.getRegisterToken());
        }
        return RestBean.noPermission();
    }

    @GetMapping("/delete")
    public RestBean<String> deleteClient(int clientId,
                                         @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.isAdminAccount(role)) {
            this.clientService.deleteClient(clientId);
            return RestBean.success();
        }
        return RestBean.noPermission();
    }

    @PostMapping("/ssh-save")
    public RestBean<Void> saveSshConnection(@RequestBody @Valid SshConnectionVO sshConnectionVO,
                                            @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                            @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, sshConnectionVO.getClientId())) {
            this.clientService.saveClientSshConnection(sshConnectionVO);
            return RestBean.success();
        }
        return RestBean.noPermission();
    }

    @GetMapping("/ssh")
    public RestBean<SshSettingsVO> sshSettings(int clientId,
                                               @RequestAttribute(Const.ATTR_USER_ID) int uid,
                                               @RequestAttribute(Const.ATTR_USER_ROLE) String role) {
        if (this.permissionCheck(uid, role, clientId)) {
            return RestBean.success(this.clientService.sshSettings(clientId));
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
        if (this.isAdminAccount(role)) return true;
        return this.accountAccessList(uid).contains(clientId);
    }

}
