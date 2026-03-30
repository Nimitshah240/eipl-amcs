package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.VoucherTypeDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.VoucherTypeSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "voucher")
public class Voucher extends BaseModelTxn {

    @Id
    private String code;
    private Boolean autoPosted;
    private Boolean cancelled;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;
    private String billNo;
    private String remarks;
    private String processName;
    private String processReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_voucher_society_code"))
    @JsonIgnoreProperties(value = {"hamlet", "village", "subDistrict", "district", "state", "route", "bmc", "mcc", "plant", "union", "branch", "bank"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherTypeSerialize.class)
    @JsonDeserialize(using = VoucherTypeDeserializer.class)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(name = "fk_voucher_voucher_type_code"))
    private VoucherType voucherType;
    private String unionCode;
    private String dockCode;
    private String financialYearsCode;

    private String xCol4;
    private String xCol5;

    @Transient
    @JsonIgnore
    private List<VoucherTransaction> voucherTransactions;

    @Override
    public String getTableName() {
        return "voucher";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        VoucherAudit audit = new VoucherAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setAutoPosted(this.getAutoPosted());
        audit.setCancelled(this.getCancelled());
        audit.setBillDate(this.getBillDate());
        audit.setVoucherDate(this.getVoucherDate());
        audit.setBillNo(this.getBillNo());
        audit.setRemarks(this.getRemarks());
        audit.setSociety(this.getSociety());
        audit.setVoucherType(this.getVoucherType());
        audit.setUnionCode(this.getUnionCode());
        audit.setDockCode(this.getDockCode());
        audit.setFinancialYearsCode(this.getFinancialYearsCode());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }


}
