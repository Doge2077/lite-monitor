package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

@Data
@Measurement(name = "runtime_detail")
public class RuntimeDetail {
    // 客户端运行时信息
    @Column(tag = true)
    Integer id;
    // 时间戳
    @Column(timestamp = true)
    Instant timestamp;
    // cpu 占用率
    @Column
    Double cpuUsage;
    // 内存占用率
    @Column
    Double memoryUsage;
    // 磁盘占用
    @Column
    Double diskUsage;
    // 网络上行
    @Column
    Double networkUpload;
    // 网络下行
    @Column
    Double networkDownload;
    // 磁盘读取
    @Column
    Double diskRead;
    // 磁盘写入
    @Column
    Double diskWrite;
}
