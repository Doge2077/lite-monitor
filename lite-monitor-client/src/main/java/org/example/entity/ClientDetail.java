package org.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDetail {
    // 客户端系统信息
    // 客户端 clientAddress 地址
    String clientAddress;
    // 系统架构
    String osArch;
    // 操作系统
    String osName;
    // 版本号
    String osVersion;
    // 操作系统位数
    Integer osBit;
    // cpu 名称
    String cpuName;
    // cpu 核心数
    Integer cpuCores;
    // 内存容量
    Double osMemory;
    // 磁盘容量
    Double diskMemory;
}
