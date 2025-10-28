package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sub_ledgers_audit")
public class SubLedgerAudit extends BaseModelAudit {

    @Id
    private String code;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;
    @Size(max = 25)
    private String referenceCode;
    private Short type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_sub_ledgers_society_code"))
    @JsonIgnoreProperties(value = {"hamlet", "village", "subDistrict", "district", "state", "route", "bmc", "mcc", "plant", "union", "branch", "bank"})
    private Society society;
    private String unionCode;

    @Override
    public String getTableName() {
        return "sub_ledgers_audit";
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
}
