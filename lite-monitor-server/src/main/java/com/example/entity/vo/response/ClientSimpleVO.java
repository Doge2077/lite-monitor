package com.example.entity.vo.response;

import lombok.Data;

@Data
public class ClientSimpleVO {
    int clientId;
    String clientName;
    String location;
    String osName;
    String osVersion;
    String clientAddress;
}
