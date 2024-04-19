package com.example.entity.vo.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class RuntimeDetailVO {
    // 客户端运行时信息
    // 时间戳
    @NonNull
    Long timestamp;
    // cpu 占用率
    @NonNull
    Double cpuUsage;
    // 内存占用率
    @NonNull
    Double memoryUsage;
    // 磁盘占用
    @NonNull
    Double diskUsage;
    // 网络上行
    @NonNull
    Double networkUpload;
    // 网络下行
    @NonNull
    Double networkDownload;
    // 磁盘读取
    @NonNull
    Double diskRead;
    // 磁盘写入
    @NonNull
    Double diskWrite;
}
