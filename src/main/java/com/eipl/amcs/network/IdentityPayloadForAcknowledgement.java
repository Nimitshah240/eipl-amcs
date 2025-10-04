package com.eipl.amcs.network;

import com.eipl.amcs.utils.CommonUtils;

import java.io.Serializable;

public class IdentityPayloadForAcknowledgement implements Serializable {
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public IdentityPayloadForAcknowledgement(String uuid) {
        this.uuid = uuid;
    }

    public IdentityPayloadForAcknowledgement() {
    }
}
