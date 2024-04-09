package com.example.utils;

import com.example.entity.dto.RuntimeDetail;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InfluxDbUtils {
    @Value("${spring.influx.url}")
    String url;

    @Value("${spring.influx.user}")
    String user;

    @Value("${spring.influx.password}")
    String password;

    private final String bucket = "lite-monitor";
    private final String org = "admin";

    private InfluxDBClient client;

    @PostConstruct
    public void initInfluxDbClient() {
         this.client = InfluxDBClientFactory.create(url, user, password.toCharArray());
    }

    public void updateRuntimeDetial(int clientId, RuntimeDetailVO runtimeDetailVO) {
        RuntimeDetail runtimeDetail = new RuntimeDetail();
        BeanUtils.copyProperties(runtimeDetailVO, runtimeDetail);
        runtimeDetail.setId(clientId);
        runtimeDetail.setTimestamp(new Date(runtimeDetailVO.getTimestamp()).toInstant());
        WriteApiBlocking writeApi = this.client.getWriteApiBlocking();
        writeApi.writeMeasurement(bucket, org, WritePrecision.NS, runtimeDetail);
    }

}
