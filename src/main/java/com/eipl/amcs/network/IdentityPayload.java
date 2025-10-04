package com.eipl.amcs.network;

import com.eipl.amcs.utils.CommonUtils;

import java.io.Serializable;

public class IdentityPayload implements Serializable {
    private String mobileNo;
    private String versionNo;

    public IdentityPayload(String mobileNo) {
        this.mobileNo = mobileNo;
        this.versionNo = CommonUtils.getVersionNo();
    }



    public IdentityPayload() {
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
}
