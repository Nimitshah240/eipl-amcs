package com.eipl.amcs.sync.model;

import java.util.Map;

public class RealTimeResponse {
    private String status;
    private RealTimeError error;
    private Map<String, Object> data;

    public RealTimeResponse() {
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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
