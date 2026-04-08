package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.LedgerTypeDeserializer;
import com.eipl.amcs.json.serialize.LedgerTypeSerialize;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ledger_groups")
public class LedgerGroup extends BaseModel {

    @Id
    private String code;
    private String name;
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerTypeSerialize.class)
    @JsonDeserialize(using = LedgerTypeDeserializer.class)
    @JoinColumn(name = "ledger_type_code", foreignKey = @ForeignKey(name = "fk_ledger_groups_ledger_type_code"))
    private LedgerType ledgerType;
    private String unionCode;
    private String refCode;
    @Override
    public String getTableName() {
        return "ledger_groups";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        LedgerGroupAudit audit = new LedgerGroupAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setUnionCode(this.getUnionCode());
        audit.setCode(this.getCode());
        audit.setName(this.getName());
        audit.setNameLocal(this.getNameLocal());
        audit.setLedgerType(this.getLedgerType());

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

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
