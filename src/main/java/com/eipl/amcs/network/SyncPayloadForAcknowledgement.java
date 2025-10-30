package com.eipl.amcs.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SyncPayloadForAcknowledgement implements Serializable {
    private String forceSyncRequestCode;
}
