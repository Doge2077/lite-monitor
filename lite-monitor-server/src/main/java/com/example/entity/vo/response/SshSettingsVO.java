package com.example.entity.vo.response;

import lombok.Data;

@Data
public class SshSettingsVO {
    String clientAddress;
    Integer port = 22;
    String username;
    String password;
}
