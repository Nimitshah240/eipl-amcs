package com.eipl.amcs.sync.model;

import com.eipl.amcs.EiplAmcsAppRunner;
import com.eipl.amcs.MainApp;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "broadcasted")
public class Broadcasted {
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

    public Broadcasted(String version) {
        this.version = version;
    }

    public static Broadcasted prepareBroadcaste(String json, String sourceType, String sourceCode, String tableName,
                                                String operation, String systemId, String version, String language) {
        Broadcasted obj = new Broadcasted();
        obj.setUuid(UUID.randomUUID().toString());
        obj.setSourceType(sourceType);
        obj.setSourceCode(sourceCode);
        obj.setTableName(tableName);
        obj.setOperation(operation);
        obj.setDataText(json);
        obj.setCreatedAt(LocalDateTime.now());
        obj.setProcessed((short) 0);
        obj.setSequence((short) 0);
        obj.setSourceSystemId(CommonUtils.getDeviceId(MainApp.identityDto.getSociety().getCode()));
        obj.setVersion(version);
        obj.setLanguage(language);
        obj.setDestCode(MainApp.identityDto.getUnion().getCode());
        obj.setDestType("UNION");
        return obj;
    }

    public BroadcastedLog toBroadcatedLog() {
        BroadcastedLog log = new BroadcastedLog();
        log.setUuid(this.getUuid());
        log.setSourceType(this.getSourceType());
        log.setSourceCode(this.getSourceCode());
        log.setDestType(this.getDestType());
        log.setDestCode(this.getDestCode());
        log.setTableName(this.getTableName());
        log.setOperation(this.getOperation());
        log.setDataText(this.getDataText());
        log.setErrorText(this.getErrorText());
        log.setCreatedAt(this.getCreatedAt());
        log.setProcessed(this.getProcessed());
        log.setSequence(this.getSequence());
        log.setProcessedAt(LocalDateTime.now());
        log.setLanguage(this.getLanguage());
        log.setSourceSystemId(this.getSourceSystemId());
        log.setVersion("d_101");
        return log;
    }

    public Inbox toInbox() {
        Inbox inbox = new Inbox();
        inbox.setUuid(this.getUuid());
        inbox.setSyncStatus("U");
        inbox.setSourceOrgType("VLC");
        inbox.setSourceOrgId(EiplAmcsAppRunner.identityDto.getIdentity().getSocietyRefCode());
        inbox.setDestOrgId(EiplAmcsAppRunner.identityDto.getUnion().getCode());
        inbox.setDestOrgType("UNION");
        inbox.setMessageType("RECORD");
        inbox.setTableName(this.getTableName());
        inbox.setOperation(this.getOperation());
        inbox.setJsonText(this.getDataText());
        inbox.setErrorLog(this.getErrorText());
        inbox.setSequenceNo(5);
        inbox.setOriginatingOrgId(EiplAmcsAppRunner.identityDto.getIdentity().getSocietyRefCode());
        inbox.setOriginatingOrgType("VLC");
        inbox.setPostingTimestamp(LocalDateTime.now());
        inbox.setSourceDeviceMac(this.getSourceSystemId());
        inbox.setVersionNo("d_101");
//        inbox.setDeviceId(EiplAmcsAppRunner.identityDto.getIdentity().getToken());
        inbox.setDeviceId("AMUL" + EiplAmcsAppRunner.identityDto.getIdentity().getSocietyCode() + "AMCS");
        return inbox;
    }
}
