package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "voucher_types")
public class VoucherType extends BaseModel {

    @Id
    private String code;
    private String name;
    private String nameLocal;
    private Integer voucherType; // 0-cash,1-bank
    @Column(name = "credit_debit")
    private Boolean creditDebit; // 0-debit,1-credit
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_voucher_types_ledger_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;
    @Override
    public String getTableName() {
        return "voucher_types";
    }


    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        VoucherTypeAudit audit = new VoucherTypeAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setName(this.getName());
        audit.setNameLocal(this.getNameLocal());
        audit.setVoucherType(this.getVoucherType());
        audit.setCreditDebit(this.getCreditDebit());
        audit.setLedger(this.getLedger());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
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
