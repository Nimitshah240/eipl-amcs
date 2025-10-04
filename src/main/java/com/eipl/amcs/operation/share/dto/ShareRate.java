package com.eipl.amcs.operation.share.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShareRate extends BaseModelTxn {
    private String code;
    private String unionCode;
    private LocalDate wefDate;
    private BigDecimal shareAmount;
    private Society society;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public LocalDate getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDate wefDate) {
        this.wefDate = wefDate;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
