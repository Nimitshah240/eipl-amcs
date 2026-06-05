package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "tbl_vendor_master")
public class Vendor extends BaseModel {

    @Id
    @Column(name = "vendor_master_code")
    private String code;

    private String vendorCode;
    private String vendorName;
    private String vendorNameLocal;
    private String panNo;
    private String aadhaarNo;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String bankAccountNo;
    private String ifsc;
    private String beneficiaryName;
    private String vendorType;
    private String xCol4;
    private String xCol5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_vendor_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_vendor_ledger"))
    @JsonIgnoreProperties(value = {"society", "ledgerGroup"})
    private Ledger ledger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_vendor_bank_code"))
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_vendor_branch_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village", "hamlet"})
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_vendor_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "tbl_vendor_master";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public String toString() {
        return this.vendorCode +" "+ CommonUtils.getLocalString(this.vendorName, this.vendorNameLocal);
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        VendorAudit audit = new VendorAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setVendorMasterCode(this.getCode());
        audit.setLedger(this.getLedger() != null ? this.getLedger() : null);
        audit.setVendorCode(this.getVendorCode());
        audit.setVendorName(this.getVendorName());
        audit.setVendorNameLocal(this.getVendorNameLocal());
        audit.setPanNo(this.getPanNo());
        audit.setAadhaarNo(this.getAadhaarNo());
        audit.setUnion(this.getUnion() != null ? this.getUnion() : null);

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setOriginatingOrgCode(this.getOriginatingOrgCode());
        audit.setOriginatingOrgType(this.getOriginatingOrgType());
        audit.setOriginatingType(this.getOriginatingType());

        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        audit.setXCol4(this.getXCol4());
        audit.setXCol5(this.getXCol5());

        audit.setBank(this.getBank() != null ? this.getBank() : null);
        audit.setBranch(this.getBranch() != null ? this.getBranch() : null);
        audit.setBankAccountNo(this.getBankAccountNo());
        audit.setIfsc(this.getIfsc());
        audit.setBeneficiaryName(this.getBeneficiaryName());
        audit.setVendorType(this.getVendorType());
        audit.setActive(this.isActive());

        return audit;
    }
}
