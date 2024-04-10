package com.example.entity.vo.response;

import lombok.Data;

@Data
public class ClientPreviewVO {
    Integer id;
    // 运行状态
    Boolean online;
    // 主机名
    String clientName;
    // 客户端 ip 地址
    String clientAddress;
    // 操作系统
    String osName;
    // 版本号
    String osVersion;
    // cpu 名称
    String cpuName;
    // cpu 核心数
    Integer cpuCores;
    // cpu 占用率
    Double cpuUsage;
    // 内存占用率
    Double memoryUsage;
    // 内存容量
    Double osMemory;
    // 网络上行
    Double networkUpload;
    // 网络下行
    Double networkDownload;
    // 地区
    String location;
}
