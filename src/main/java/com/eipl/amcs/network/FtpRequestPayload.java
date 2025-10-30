package com.eipl.amcs.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FtpRequestPayload implements Serializable {
    private String deviceDateTime;
    private String deviceId;
    private String eiplCode;
    private String imeiNo;
    private String latLong;
    private String versionNo;
}