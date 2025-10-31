package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.MilkDispatchDeserializer;
import com.eipl.amcs.json.deserialize.MilkQualityTypeDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.serialize.MilkDispatchSerialize;
import com.eipl.amcs.json.serialize.MilkQualityTypeSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milk_dispatch_transaction_audit")
public class MilkDispatchTransactionAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String txnCode;
    private String societyPurchaseRateCode;
    private String chamberNo;
    private BigDecimal acidity;
    private BigDecimal amount;
    private BigDecimal avgClr;
    private BigDecimal avgFat;
    private BigDecimal avgSnf;
    private BigDecimal density;
    private BigDecimal freezingPoint;
    private BigDecimal lactose;
    private BigDecimal protein;
    private BigDecimal rate;
    private BigDecimal temp;
    private BigDecimal water;
    private BigDecimal convertedQuantity;
    private BigDecimal qty;
    private Integer nosOfCan;
    private Integer quantityMode;
    private Integer convertedQuantityMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkDispatchSerialize.class)
    @JsonDeserialize(using = MilkDispatchDeserializer.class)
    @JoinColumn(name = "challan_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"fromShift", "toShift", "society", "union"})
    private MilkDispatch milkDispatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkType milkType;

    @Override
    public String getTableName() {
        return "milk_dispatch_transaction_audit";
    }

}
