package com.eipl.amcs.base.model;


public class Identity extends BaseModelTxn {

    private String code;
    private String token;
    private String societyRefCode;
    private String systemMac;
    private String syncStatus;
    private String syncUrl;
    private String unionCode;
    private String societyCode;
    private String dockNo;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSocietyRefCode() {
        return societyRefCode;
    }

    public void setSocietyRefCode(String societyRefCode) {
        this.societyRefCode = societyRefCode;
    }

    public String getSystemMac() {
        return systemMac;
    }

    public void setSystemMac(String systemMac) {
        this.systemMac = systemMac;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncUrl() {
        return syncUrl;
    }

    public void setSyncUrl(String syncUrl) {
        this.syncUrl = syncUrl;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }
}
