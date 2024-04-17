package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ChangePasswordVO;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.utils.Const;

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

}
