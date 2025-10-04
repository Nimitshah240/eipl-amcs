package com.eipl.amcs.network;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RealTimeRequest<T> implements Serializable {
    private String deviceId;
    private String identityCode;
    private String imei;
    private String organizationCode;
    private String organizationType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;
    private String token;
    private T content;

    public RealTimeRequest(String societyCode, String token, T content) {
        this.token = token;
        this.deviceId = CommonUtils.getDeviceId(societyCode);
        this.organizationCode= societyCode;
        this.organizationType="VLC";
//        this.organizationType="SOCIETY";
        this.imei = "";
        this.token = token;
        this.identityCode = societyCode;
        this.requestTime = LocalDateTime.now();
        this.content = content;
    }

    public RealTimeRequest() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }


}
