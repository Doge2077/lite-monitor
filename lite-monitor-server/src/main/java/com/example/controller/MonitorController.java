package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
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

    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO renameClientVO) {
        this.clientService.renameClient(renameClientVO);
        return RestBean.success();
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId) {
        return RestBean.success(this.clientService.clientDetails(clientId));
    }

}
