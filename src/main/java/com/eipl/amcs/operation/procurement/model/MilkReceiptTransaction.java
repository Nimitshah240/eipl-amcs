package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.MilkDispatchDeserializer;
import com.eipl.amcs.json.deserialize.MilkQualityTypeDeserializer;
import com.eipl.amcs.json.deserialize.MilkReceiptDeserializer;
import com.eipl.amcs.json.serialize.MilkDispatchSerialize;
import com.eipl.amcs.json.serialize.MilkQualityTypeSerialize;
import com.eipl.amcs.json.serialize.MilkReceiptSerialize;
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
@Table(name = "milk_receipt_transaction")
public class MilkReceiptTransaction extends BaseModelTxn {

    @Id
    private String txnCode;
    private String societyPurchaseRateCode;
    private BigDecimal avgClr;
    private BigDecimal avgFat;
    private BigDecimal avgSnf;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal acidity;
    private BigDecimal density;
    private BigDecimal freezingPoint;
    private BigDecimal lactose;
    private BigDecimal protein;
    private BigDecimal temp;
    private BigDecimal water;
    private BigDecimal convertedQuantity;
    private BigDecimal qty;
    private Integer quantityMode;
    private BigDecimal convertedQuantityMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkReceiptSerialize.class)
    @JsonDeserialize(using = MilkReceiptDeserializer.class)
    @JoinColumn(name = "milk_receipt_code", foreignKey = @ForeignKey(name = "fk_milk_receipt_transaction_milk_receipt_code"))
    @JsonIgnoreProperties(value = {"fromShift", "toShift", "society", "union"})
    private MilkReceipt milkReceipt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_milk_dispatch_transaction_milk_quality_type_code"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkDispatchSerialize.class)
    @JsonDeserialize(using = MilkDispatchDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_milk_dispatch_transaction_milk_type_code"))
    private MilkType milkType;

    @Override
    public String getTableName() {
        return "milk_receipt_transaction";
    }

    @Override
    public Object getId() {
        return this.getTxnCode();
    }
}
