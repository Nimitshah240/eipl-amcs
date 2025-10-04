package com.eipl.amcs.network;

import java.util.List;

public class RealTimeError {
    private int code;
    private List<String> message;

    public RealTimeError() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
