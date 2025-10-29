package com.eipl.amcs.sync.model;

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
@Table(name = "subscribed_error")
public class SubscribedError {
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
    private LocalDateTime createdAt;
    private LocalDateTime receivedAt;
    private short processed; // Default = 0
    private short sequence; // Default = 0
    private LocalDateTime processedAt;
    private String language;
    private String sourceSystemId;
    private String version;
}
