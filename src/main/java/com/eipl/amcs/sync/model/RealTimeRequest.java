package com.eipl.amcs.sync.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeRequest<T> implements Serializable {
    private String deviceId;
    private String identityCode;
    private String imei;
    private String organizationCode;
    private String organizationType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime requestTime;
    private String token;
    private T content;
}
