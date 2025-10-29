package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyPaymentCycleDeserializer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietyPaymentCycleSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_sale_installment_audit")
public class ProductSaleInstallmentAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 40)
    private String code;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal actualInstallment;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal installmentAmount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal previousPendingAmount;
    @Column(name = "is_billing")
    private Boolean billing;
    @Size(max = 35)
    private String invoiceNo;
    private LocalDate deductionDate;
    @Size(max = 10)
    private String unionCode;
    @Size(max = 10)
    private String societyCode;
    private Integer type; // 1-product, 2-service, 3- cash adv, 4-farmer bill head

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietyPaymentCycleSerialize.class)
    @JsonDeserialize(using = SocietyPaymentCycleDeserializer.class)
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SocietyPaymentCycle societyPaymentCycle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Override
    public String getTableName() {
        return "product_sale_installment_audit";
    }
}
