package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.SocietyPaymentCycleDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietyPaymentCycleSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_bill")
public class MemberBill extends BaseModelTxn {
    @Id
    private String code;
    private BigDecimal milkQty;
    private BigDecimal avgFat;
    private BigDecimal avgSnf;
    private BigDecimal avgClr;
    private BigDecimal kgFat;
    private BigDecimal kgSnf;
    private BigDecimal milkAmount;
    private BigDecimal productSaleAmount;
    private BigDecimal localSaleAmount;
    private BigDecimal loanAmount;
    private BigDecimal otherAddAmount;
    private BigDecimal otherDedAmount;
    private BigDecimal netAmount;

    private String voucherNo;
    private short paymentMode; //0-Cash, 1-Bank
    private String bankAcno;
    private String ifsc;
    private String paymentRef;

    //	private BooleanProperty selected;
    private short status; //1-PENDING, 2-SENT, 3-SENT_DBT, 4-REJECT, 5-PROCESSING, 6-DISBURSED, 7-FAIL

    @Column(name = "is_disbursed")
    private boolean disbursed;
    private LocalDate disbursedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_member_bill_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_member_bill_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_member_bill_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietyPaymentCycleSerialize.class)
    @JsonDeserialize(using = SocietyPaymentCycleDeserializer.class)
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(name = "fk_member_bill_payment_cycle_code"))
    @JsonIgnoreProperties(value = {"society", "fromShift", "toShift"})
    private SocietyPaymentCycle paymentCycle;

    @Override
    public String getTableName() {
        return "member_bill";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberBillAudit audit = new MemberBillAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setMilkAmount(this.getMilkAmount());
        audit.setMilkQty(this.getMilkQty());
        audit.setMember(this.getMember());
        audit.setAvgClr(this.getAvgClr());
        audit.setAvgSnf(this.getAvgSnf());
        audit.setAvgFat(this.getAvgFat());
        audit.setProductSaleAmount(this.getProductSaleAmount());
        audit.setLocalSaleAmount(this.getLocalSaleAmount());
        audit.setLoanAmount(this.getLoanAmount());
        audit.setOtherAddAmount(this.getOtherAddAmount());
        audit.setOtherDedAmount(this.getOtherDedAmount());
        audit.setNetAmount(this.getNetAmount());
        audit.setVoucherNo(this.getVoucherNo());
        audit.setPaymentMode(this.getPaymentMode());
        audit.setBankAcno(this.getBankAcno());
        audit.setIfsc(this.getIfsc());
        audit.setPaymentRef(this.getPaymentRef());
        audit.setStatus(this.getStatus());
        audit.setDisbursedDate(this.getDisbursedDate());
        audit.setDisbursed(this.isDisbursed());
        audit.setSociety(this.getSociety());
        audit.setUnion(this.getUnion());
        audit.setMember(this.getMember());
        audit.setPaymentCycle(this.getPaymentCycle());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    public short getPaymnetMode() {
        return paymentMode;
    }

    public void setPaymnetMode(short paymnetMode) {
        this.paymentMode = paymnetMode;
    }
}
