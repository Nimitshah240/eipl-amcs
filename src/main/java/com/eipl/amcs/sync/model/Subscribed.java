package com.eipl.amcs.sync.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subscribed")
public class Subscribed {
    @Id
    private String uuid;
    private String sourceType;
    private String sourceCode;
    private String destType;
    private String destCode;
    private String tableName;
    private String operation; // INSERT, UPDATE, DELETE
    private String dataText;
    private String errorText;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime receivedAt;
    private short processed; // Default = 0
    private short sequence; // Default = 0
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime processedAt;
    private String language;
    private String sourceSystemId;
    private String version;


    public BroadcastedLog toSubscribedLog(Subscribed subscribed) {
        BroadcastedLog log = new BroadcastedLog();
        log.setUuid(subscribed.getUuid());
        log.setSourceType(subscribed.getSourceType());
        log.setSourceCode(subscribed.getSourceCode());
        log.setDestType(subscribed.getDestType());
        log.setDestCode(subscribed.getDestCode());
        log.setTableName(subscribed.getTableName());
        log.setOperation(subscribed.getOperation());
        log.setDataText(subscribed.getDataText());
        log.setErrorText(subscribed.getErrorText());
        log.setCreatedAt(subscribed.getCreatedAt());
        log.setProcessed(subscribed.getProcessed());
        log.setSequence(subscribed.getSequence());
        log.setProcessedAt(subscribed.getProcessedAt());
        log.setLanguage(subscribed.getLanguage());
        log.setSourceSystemId(subscribed.getSourceSystemId());
        log.setVersion(subscribed.getVersion());
        return log;
    }
}

