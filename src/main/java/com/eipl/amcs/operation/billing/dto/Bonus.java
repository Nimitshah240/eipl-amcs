package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bonus extends BaseModel {

    private String code;
    private BigDecimal milkQty;
    private BigDecimal milkAmount;
    private BigDecimal bonusAmount;
    private short status; //0-PENDING,1-DISBURSED
    private short type; //0-Union,1-Society
    private BonusSummary bonusSummary;
    private Member member;

    private Society society;
    private Union union;

    private BooleanProperty selected;
    public Bonus() {
        selected = new SimpleBooleanProperty();
    }

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }

    public final boolean isSelected() {
        return this.selectedProperty().get();
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

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMilkQty() {
        return milkQty;
    }

    public void setMilkQty(BigDecimal milkQty) {
        this.milkQty = milkQty;
    }

    public BigDecimal getMilkAmount() {
        return milkAmount;
    }

    public void setMilkAmount(BigDecimal milkAmount) {
        this.milkAmount = milkAmount;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public BonusSummary getBonusSummary() {
        return bonusSummary;
    }

    public void setBonusSummary(BonusSummary bonusSummary) {
        this.bonusSummary = bonusSummary;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
