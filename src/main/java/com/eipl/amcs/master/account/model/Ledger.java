package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name = "ledgers")
public class Ledger extends BaseModel {

    @Id
    @Size(max = 15)
    private String code;

    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;
    private Boolean hasSubLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_group_code", foreignKey = @ForeignKey(name = "fk_ledgers_ledger_group_code"))
    @JsonIgnoreProperties(value = {"ledgerType"})
    private LedgerGroup ledgerGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledgers_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;
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
        audit.setSociety(this.getSociety());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setActive(this.isActive());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

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
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
