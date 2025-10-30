package com.eipl.amcs.network;

import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
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
        this.organizationCode = societyCode;
        this.organizationType = "VLC";
//        this.organizationType="SOCIETY";
        this.imei = "";
        this.token = token;
        this.identityCode = societyCode;
        this.requestTime = LocalDateTime.now();
        this.content = content;
    }

    public RealTimeRequest() {
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getToken() {
        return token;
    }

}
