package com.eipl.amcs.base.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notification")
public class Notification extends BaseModelTxn {

    @Id
    private Integer bulkNotificationId;
    private String societyCode;
    private String unionCode;
    private String mccPlantCode;
    private String bmcCode;
    private String plantCode;
    private String memberCode;
    private String appType;
    private String loginType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime wefDate;
    private String title;
    private String message;
    private String campaignName;
    private Integer receiverType;
    private String contentId;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime entry;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime pickup;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime response;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime toDate;
    private Integer fromShift;
    private Integer toShift;
    private Integer NotificationType;
    private String fileName;
    private String filePath;

    @Override
    public String getTableName() {
        return "notification";
    }
}
