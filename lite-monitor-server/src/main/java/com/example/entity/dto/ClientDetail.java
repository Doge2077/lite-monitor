package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("db_client_detail")
public class ClientDetail {
    @TableId
    Integer clientId;
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
