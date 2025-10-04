package com.eipl.amcs.network;

import java.util.List;
import java.util.Map;

public class RealTimeMultipleResponse {
    private String status;
    private RealTimeError error;
    private List<Map<String, Object>> data;

    public RealTimeMultipleResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealTimeError getError() {
        return error;
    }

    public void setError(RealTimeError error) {
        this.error = error;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
