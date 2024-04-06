package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("db_runtime_detail")
public class RuntimeDetail {
    // 客户端运行时信息
    @TableId
    Integer id;
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
