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
@Table(name = "broadcasted_log")
public class BroadcastedLog {
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
    private short processed; // Default = 0
    private short sequence; // Default = 0
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime processedAt;
    private String language;
    private String sourceSystemId;
    private String version;
}
