package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.service.ClientService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/monitor")
public class ServerController {

    @Resource
    ClientService clientService;

    @GetMapping("/register")
    public RestBean<Void> registerClient(@RequestHeader("Authorization") String token) {
        if (this.clientService.verifyAndRegister(token)) {
            return RestBean.success();
        }
        return RestBean.failure(401, "注册失败，Token无效");
    }

    @PostMapping("/detail")
    public RestBean<Void> updateClientDetails(@RequestAttribute(Const.ATTR_CLIENT) Client client,
                                              @RequestBody ClientDetailVO clientDetailVO) {
        Boolean success = clientService.updateClientDetail(clientDetailVO, client);
        return success ? RestBean.success() : RestBean.failure(404, "数据非法，请联系管理员");
    }

}
