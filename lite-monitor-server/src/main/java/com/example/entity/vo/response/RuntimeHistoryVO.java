package com.example.entity.vo.response;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class RuntimeHistoryVO {
    Double diskMemory;
    Double memoryUsage;
    List<JSONObject> list = new LinkedList<>();
}
