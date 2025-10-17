package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milk_dispatch_audit")
public class MilkDispatchAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 35)
    private String challanNo;
    private String destinationCode;
    private BigDecimal dipStickReadingClosing;
    private BigDecimal dipStickReadingOpening;
    private Integer dispatchType;
    private Integer destinationType;
    private BigDecimal headLoadKms;
    private String routeNo;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_shift_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_shift_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Shift toShift;
    private LocalTime vehicleInTime;
    private LocalTime vehicleOutTime;
    private String vehicleNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Union union;
    private String brokenSealNo;
    private String newSealNo;

    @Override
    public String getTableName() {
        return "milk_dispatch_audit";
    }

}
