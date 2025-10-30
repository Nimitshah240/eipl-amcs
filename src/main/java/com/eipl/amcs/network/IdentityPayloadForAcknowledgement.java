package com.eipl.amcs.network;

import java.io.Serializable;

public class IdentityPayloadForAcknowledgement implements Serializable {
    private String uuid;

    public IdentityPayloadForAcknowledgement(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
