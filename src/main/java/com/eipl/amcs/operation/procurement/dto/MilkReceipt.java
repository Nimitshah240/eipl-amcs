package com.eipl.amcs.operation.procurement.dto;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;

public class MilkReceipt extends BaseModel {
    private String code;
    private MilkDispatch milkDispatch;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Shift fromShift;
    private Shift toShift;
    private Society society;
    private Union union;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MilkDispatch getMilkDispatch() {
        return milkDispatch;
    }

    public void setMilkDispatch(MilkDispatch milkDispatch) {
        this.milkDispatch = milkDispatch;
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

    public Shift getFromShift() {
        return fromShift;
    }

    public void setFromShift(Shift fromShift) {
        this.fromShift = fromShift;
    }

    public Shift getToShift() {
        return toShift;
    }

    public void setToShift(Shift toShift) {
        this.toShift = toShift;
    }


    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }




}
