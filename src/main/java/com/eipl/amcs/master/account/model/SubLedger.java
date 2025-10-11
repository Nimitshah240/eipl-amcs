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
@Table(name = "sub_ledgers")
public class SubLedger extends BaseModel {

    @Id
    private String code;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;
    @Size(max = 25)
    private String referenceCode;
    private Short type;

    @Transient
    private BooleanProperty selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_sub_ledgers_society_code"))
    @JsonIgnoreProperties(value = {"hamlet", "village", "subDistrict", "district", "state", "route", "bmc", "mcc", "plant", "union", "branch", "bank"})
    private Society society;
    private String unionCode;

    @Override
    public String getTableName() {
        return "sub_ledgers";
    }

    public SubLedger() {
        selected = new SimpleBooleanProperty(false);
    }

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        SubLedgerAudit audit = new SubLedgerAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setName(this.getName());
        audit.setNameLocal(this.getNameLocal());
        audit.setReferenceCode(this.getReferenceCode());
        audit.setType(this.getType());
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

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
