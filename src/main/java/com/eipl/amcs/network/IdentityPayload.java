package com.eipl.amcs.network;

import com.eipl.amcs.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdentityPayload implements Serializable {
    private String mobileNo;
    private String versionNo;

    public IdentityPayload(String mobileNo) {
        this.mobileNo = mobileNo;
        this.versionNo = CommonUtils.getVersionNo();
    }
}
