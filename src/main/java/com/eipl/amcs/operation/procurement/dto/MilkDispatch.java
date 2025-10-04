package com.eipl.amcs.operation.procurement.dto;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MilkDispatch extends BaseModel {
    private String challanNo;
    private String destinationCode;
    private BigDecimal dipStickReadingClosing;
    private BigDecimal dipStickReadingOpening;
    private Integer dispatchType;
    private Integer destinationType;
    private BigDecimal headLoadKms;
    private String routeNo;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Shift fromShift;
    private Shift toShift;
    private LocalTime vehicleInTime;
    private LocalTime vehicleOutTime;
    private String vehicleNo;
    private Society society;
    private Union union;
    private String brokenSealNo;
    private String newSealNo;

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public BigDecimal getDipStickReadingClosing() {
        return dipStickReadingClosing;
    }

    public void setDipStickReadingClosing(BigDecimal dipStickReadingClosing) {
        this.dipStickReadingClosing = dipStickReadingClosing;
    }

    public BigDecimal getDipStickReadingOpening() {
        return dipStickReadingOpening;
    }

    public void setDipStickReadingOpening(BigDecimal dipStickReadingOpening) {
        this.dipStickReadingOpening = dipStickReadingOpening;
    }

    public Integer getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(Integer dispatchType) {
        this.dispatchType = dispatchType;
    }

    public Integer getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(Integer destinationType) {
        this.destinationType = destinationType;
    }

    public BigDecimal getHeadLoadKms() {
        return headLoadKms;
    }

    public void setHeadLoadKms(BigDecimal headLoadKms) {
        this.headLoadKms = headLoadKms;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
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

    public LocalTime getVehicleInTime() {
        return vehicleInTime;
    }

    public void setVehicleInTime(LocalTime vehicleInTime) {
        this.vehicleInTime = vehicleInTime;
    }

    public LocalTime getVehicleOutTime() {
        return vehicleOutTime;
    }

    public void setVehicleOutTime(LocalTime vehicleOutTime) {
        this.vehicleOutTime = vehicleOutTime;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
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

    public String getBrokenSealNo() {
        return brokenSealNo;
    }

    public void setBrokenSealNo(String brokenSealNo) {
        this.brokenSealNo = brokenSealNo;
    }

    public String getNewSealNo() {
        return newSealNo;
    }

    public void setNewSealNo(String newSealNo) {
        this.newSealNo = newSealNo;
    }


    @Override
    public String toString() {
        return getFromDate().toLocalDate()+"-"+getFromShift().getName().substring(0,1)+" "+
                getToDate().toLocalDate()+"-"+getToShift().getName().substring(0,1);
    }
}
