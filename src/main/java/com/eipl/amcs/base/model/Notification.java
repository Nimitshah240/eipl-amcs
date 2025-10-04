package com.eipl.amcs.base.model;


import java.time.LocalDateTime;

public class Notification extends BaseModelTxn {

    private Integer bulkNotificationId;
    private String societyCode;
    private String unionCode;
    private String plantCode;
    private String bmcCode;
    private String MccPlantCode;
    private String memberCode;
    private String appType;
    private String loginType;
    private LocalDateTime wefDate;
    private String title;
    private String message;
    private String campaignName;
    private Integer receiverType;
    private String contentId;
    private Integer status;
    private LocalDateTime entry;
    private LocalDateTime pickup;
    private LocalDateTime response;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Integer fromShift;
    private Integer toShift;
    private Integer NotificationType;

    //<option value="1">Alert</option>
    //<option value="2">Priptra</option>
    //<option value="3">Special Message</option>
    //<option value="4">Milk Bill</option>
    //<option value="5">Bacteria Test</option>
    //<option value="6">Eipl Bill</option>

    private String fileName;
    private String filePath;

    public Integer getBulkNotificationId() {
        return bulkNotificationId;
    }

    public void setBulkNotificationId(Integer bulkNotificationId) {
        this.bulkNotificationId = bulkNotificationId;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getBmcCode() {
        return bmcCode;
    }

    public void setBmcCode(String bmcCode) {
        this.bmcCode = bmcCode;
    }

    public String getMccPlantCode() {
        return MccPlantCode;
    }

    public void setMccPlantCode(String mccPlantCode) {
        MccPlantCode = mccPlantCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public LocalDateTime getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDateTime wefDate) {
        this.wefDate = wefDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getEntry() {
        return entry;
    }

    public void setEntry(LocalDateTime entry) {
        this.entry = entry;
    }

    public LocalDateTime getPickup() {
        return pickup;
    }

    public void setPickup(LocalDateTime pickup) {
        this.pickup = pickup;
    }

    public LocalDateTime getResponse() {
        return response;
    }

    public void setResponse(LocalDateTime response) {
        this.response = response;
    }

    public String getOriginatingOrgCode() {
        return originatingOrgCode;
    }

    public void setOriginatingOrgCode(String originatingOrgCode) {
        this.originatingOrgCode = originatingOrgCode;
    }

    public String getOriginatingOrgType() {
        return originatingOrgType;
    }

    public void setOriginatingOrgType(String originatingOrgType) {
        this.originatingOrgType = originatingOrgType;
    }

    public Integer getOriginatingType() {
        return originatingType;
    }

    public void setOriginatingType(Integer originatingType) {
        this.originatingType = originatingType;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public Integer getFromShift() {
        return fromShift;
    }

    public void setFromShift(Integer fromShift) {
        this.fromShift = fromShift;
    }

    public Integer getToShift() {
        return toShift;
    }

    public void setToShift(Integer toShift) {
        this.toShift = toShift;
    }

    public Integer getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(Integer notificationType) {
        NotificationType = notificationType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
