package com.example.entity.vo.response;

import lombok.Data;

@Data
public class ClientDetailsVO {
    Integer clientId;
    String clientName;
    Boolean online;
    String node;
    String location;
    String clientAddress;
    String cpuName;
    String osName;
    String osVersion;
    Double osMemory;
    Integer cpuCores;
    Double diskMemory;
}
