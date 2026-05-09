package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.LedgerGroupDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.LedgerGroupSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name = "ledgers")
public class Ledger extends BaseModel {

    @Id
    private String code;

    private String name;
    private String nameLocal;
    private Boolean hasSubLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerGroupSerialize.class)
    @JsonDeserialize(using = LedgerGroupDeserializer.class)
    @JoinColumn(name = "ledger_group_code", foreignKey = @ForeignKey(name = "fk_ledgers_ledger_group_code"))
    @JsonIgnoreProperties(value = {"ledgerType"})
    private LedgerGroup ledgerGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledgers_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;
    private String plantCode;
    private String mccCode;
    private String bmcCode;
    private String refCode;

    @Transient
    private BooleanProperty selected;

    public Ledger() {
        selected = new SimpleBooleanProperty(false);
    }

    public Ledger(String name) {
        selected = new SimpleBooleanProperty(false);

        this.name = name;
    }

    @Override
    public String getTableName() {
        return "ledgers";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        LedgerAudit audit = new LedgerAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setName(this.getName());
        audit.setNameLocal(this.getNameLocal());
        audit.setHasSubLedger(this.getHasSubLedger());
        audit.setLedgerGroup(this.getLedgerGroup());
        audit.setUnionCode(this.getUnionCode());
        audit.setPlantCode(this.getPlantCode());
        audit.setMccCode(this.getMccCode());
        audit.setBmcCode(this.getBmcCode());
        audit.setSociety(this.getSociety());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setActive(this.isActive());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        audit.setRefCode(this.getRefCode());

        return audit;
    }

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }

    public final boolean isSelected() {
        return this.selectedProperty().get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public String toString() {
        return (this.refCode == null ? "" : this.refCode) + ' ' + CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
