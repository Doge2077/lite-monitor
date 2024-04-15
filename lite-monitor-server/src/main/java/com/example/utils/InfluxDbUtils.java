package com.example.utils;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.dto.RuntimeDetail;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class InfluxDbUtils {
    @Value("${spring.influx.url}")
    String url;

    @Value("${spring.influx.user}")
    String user;

    @Value("${spring.influx.password}")
    String password;

    private final String BUCKET = "lite-monitor";
    private final String ORG = "admin";

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
        writeApi.writeMeasurement(BUCKET, ORG, WritePrecision.NS, runtimeDetail);
    }

     // 查询一个小时内的历史数据
    public RuntimeHistoryVO readRuntimeData(int clientId) {
        RuntimeHistoryVO runtimeHistoryVO = new RuntimeHistoryVO();
        String query = """
                from(bucket: "%s")
                |> range(start: %s)
                |> filter(fn: (r) => r["_measurement"] == "runtime_detail")
                |> filter(fn: (r) => r["id"] == "%s")
                """;
        String formatedQuery = String.format(query, BUCKET, "-1h", clientId);
        List<FluxTable> fluxTableList = this.client.getQueryApi().query(formatedQuery, ORG);
        int size = fluxTableList.size();
        if (size == 0) return runtimeHistoryVO;
        List<FluxRecord> fluxRecordList = fluxTableList.get(0).getRecords();
        for (int i = 0; i < fluxRecordList.size(); i ++) {
            JSONObject object = new JSONObject();
            object.put("timestamp", fluxRecordList.get(i).getTime());
            for (int j = 0; j < size; j ++) {
                FluxRecord fluxRecord = fluxTableList.get(j).getRecords().get(i);
                object.put(fluxRecord.getField(), fluxRecord.getValue());
            }
            runtimeHistoryVO.getList().add(object);
        }
        return runtimeHistoryVO;
    }

}
