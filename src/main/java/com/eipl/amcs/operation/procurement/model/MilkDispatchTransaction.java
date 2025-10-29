package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.MilkDispatchDeserializer;
import com.eipl.amcs.json.deserialize.MilkQualityTypeDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.json.serialize.MilkDispatchSerialize;
import com.eipl.amcs.json.serialize.MilkQualityTypeSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milk_dispatch_transaction")
public class MilkDispatchTransaction extends BaseModelTxn {

    @Id
    private String txnCode;
    @Size(max = 11)
    private String societyPurchaseRateCode;
    //	@Size(max = 35)
//	private String challanNo;
    @Size(max = 255)
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
    @JoinColumn(name = "challan_no", foreignKey = @ForeignKey(name = "fk_milk_dispatch_transaction_challan_no"))
    @JsonIgnoreProperties(value = {"fromShift", "toShift", "society", "union"})
    private MilkDispatch milkDispatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_milk_dispatch_transaction_milk_quality_types"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_milk_dispatch_transaction_milk_types"))
    private MilkType milkType;

    @Override
    public String getTableName() {
        return "milk_dispatch_transaction";
    }

    @Override
    public Object getId() {
        return this.getTxnCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MilkDispatchTransactionAudit audit = new MilkDispatchTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setTxnCode(this.getTxnCode());
        audit.setChamberNo(this.getChamberNo());
        audit.setAcidity(this.getAcidity());
        audit.setAmount(this.getAmount());
        audit.setAvgClr(this.getAvgClr());
        audit.setAvgFat(this.getAvgFat());
        audit.setAvgSnf(this.getAvgSnf());
        audit.setDensity(this.getDensity());
        audit.setFreezingPoint(this.getFreezingPoint());
        audit.setLactose(this.getLactose());
        audit.setProtein(this.getProtein());
        audit.setRate(this.getRate());
        audit.setTemp(this.getRate());
        audit.setWater(this.getWater());
        audit.setConvertedQuantity(this.getConvertedQuantity());
        audit.setQty(this.getQty());
        audit.setNosOfCan(this.getNosOfCan());
        audit.setConvertedQuantityMode(this.getConvertedQuantityMode());
        audit.setMilkDispatch(this.getMilkDispatch());
        audit.setMilkQualityType(this.getMilkQualityType());
        audit.setMilkType(this.getMilkType());

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
