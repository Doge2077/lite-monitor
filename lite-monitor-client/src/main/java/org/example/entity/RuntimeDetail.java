package org.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuntimeDetail {
    // 客户端运行时信息
    // 时间戳
    Long timestamp;
    // cpu 占用率
    Double cpuUsage;
    // 内存占用率
    Double memoryUsage;
    // 磁盘占用
    Double diskUsage;
    // 网络上行
    Double networkUpload;
    // 网络下行
    Double networkDownload;
    // 磁盘读取
    Double diskRead;
    // 磁盘写入
    Double diskWrite;
}
