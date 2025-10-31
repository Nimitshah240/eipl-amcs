package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.SocietyPaymentCycleDeserializer;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietyPaymentCycleSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "cash_advance")
public class CashAdvance extends BaseModelTxn {

    @Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    private BigDecimal amount;
    private Integer noOfInstallment;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate installmentDate;
    private String unionCode;
    private String voucherNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_cash_advance_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietyPaymentCycleSerialize.class)
    @JsonDeserialize(using = SocietyPaymentCycleDeserializer.class)
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(name = "fk_cash_advance_society_payment_cycle_code"))
    @JsonIgnoreProperties(value = {"society", "fromShift", "toShift", "milkType"})
    private SocietyPaymentCycle societyPaymentCycle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_cash_advance_member_code"))
    @JsonIgnoreProperties(value = {"memberType", "society", "milkType"})
    private Member member;

    @Override
    public String getTableName() {
        return "cash_advance";
    }
}
