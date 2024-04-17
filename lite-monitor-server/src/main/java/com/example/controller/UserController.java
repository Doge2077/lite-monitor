package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ChangePasswordVO;
import com.example.entity.vo.request.CreateSubAccountVO;
import com.example.entity.vo.response.SubAccountVO;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.utils.Const;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    AccountService accountService;

    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestBody @Valid ChangePasswordVO changePasswordVO,
                                         @RequestAttribute(Const.ATTR_USER_ID) int userId) {
        return this.accountService.changePassword(userId, changePasswordVO.getPassword(), changePasswordVO.getNew_password()) ?
                RestBean.success() : RestBean.failure(401, "原密码输入错误");
    }

    @PostMapping("/sub/create")
    public RestBean<Void> createSubAccount(@RequestBody @Valid CreateSubAccountVO createSubAccountVO) {
        this.accountService.createSubAccount(createSubAccountVO);
        return RestBean.success();
    }

    @GetMapping("/sub/delete")
    public RestBean<Void> deleteSubAccount(int uid,
                                           @RequestAttribute(Const.ATTR_USER_ID) int userId) {
        if(uid == userId) {
            return RestBean.failure(401, "非法参数");
        }
        this.accountService.deleteSubAccount(uid);
        return RestBean.success();
    }

    @GetMapping("/sub/list")
    public RestBean<List<SubAccountVO>> subAccountList() {
        return RestBean.success(this.accountService.listSubAccount());
    }

}
