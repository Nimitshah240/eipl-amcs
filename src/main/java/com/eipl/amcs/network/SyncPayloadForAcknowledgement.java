package com.eipl.amcs.network;

import java.io.Serializable;

public class SyncPayloadForAcknowledgement implements Serializable {
    private String forceSyncRequestCode;

    public SyncPayloadForAcknowledgement(String forceSyncRequestCode) {
        this.forceSyncRequestCode = forceSyncRequestCode;
    }

    public SyncPayloadForAcknowledgement() {
    }

    public String getForceSyncRequestCode() {
        return forceSyncRequestCode;
    }

    public void setForceSyncRequestCode(String forceSyncRequestCode) {
        this.forceSyncRequestCode = forceSyncRequestCode;
    }
}
