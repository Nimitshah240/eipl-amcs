package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "society_payment_cycles_audit")
public class SocietyPaymentCycleAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Integer intervalValue;
    @Column(name = "is_billing")
    private Boolean billing;
    @Column(name = "lock_billing_process")
    private Boolean lockBillingProcess;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "from_shift_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "to_shift_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Shift toShift;

    @Override
    public String getTableName() {
        return "society_payment_cycles_audit";
    }

}
