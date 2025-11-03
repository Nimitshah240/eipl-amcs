package com.eipl.amcs.sync.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Inbox {
    private String uuid;
    private String syncStatus;
    private String sourceOrgType;
    private String sourceOrgId;
    private String destOrgType;
    private String destOrgId;
    private String messageType;
    private String tableName;
    private String operation;
    private String jsonText;
    private String errorLog;
    private Integer sequenceNo;
    private String originatingOrgId;
    private String originatingOrgType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime postingTimestamp;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime syncTimestamp;
    private String sourceDeviceMac;
    private String versionNo;
    private String deviceId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime errorTimestamp;
}
