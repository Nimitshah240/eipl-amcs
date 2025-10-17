package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JoinColumn(name = "milk_receipt_code", foreignKey = @ForeignKey(name = "fk_milk_receipt_transaction_milk_receipt_code"))
    @JsonIgnoreProperties(value = {"fromShift", "toShift", "society", "union"})
    private MilkReceipt milkReceipt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_milk_dispatch_transaction_milk_quality_type_code"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
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

//	@Override
//	public JsonAndTableBuilder getAuditModel(String operation, String user) {
//		MilkDispatchTransactionAudit audit = new MilkDispatchTransactionAudit();
//		audit.setOperationType(operation);
//		audit.setAuditCreatedBy(user);
//
//		audit.setTxnCode(this.getTxnCode());
//		audit.setChamberNo(this.getChamberNo());
//		audit.setAcidity(this.getAcidity());
//		audit.setAmount(this.getAmount());
//		audit.setAvgClr(this.getAvgClr());
//		audit.setAvgFat(this.getAvgFat());
//		audit.setAvgSnf(this.getAvgSnf());
//		audit.setDensity(this.getDensity());
//		audit.setFreezingPoint(this.getFreezingPoint());
//		audit.setLactose(this.getLactose());
//		audit.setProtein(this.getProtein());
//		audit.setRate(this.getRate());
//		audit.setTemp(this.getRate());
//		audit.setWater(this.getWater());
//		audit.setConvertedQuantity(this.getConvertedQuantity());
//		audit.setQty(this.getQty());
//		audit.setNosOfCan(this.getNosOfCan());
//		audit.setConvertedQuantityMode(this.getConvertedQuantityMode());
//		audit.setMilkDispatch(this.getMilkDispatch());
//		audit.setMilkQualityType(this.getMilkQualityType());
//		audit.setMilkType(this.getMilkType());
//
//		audit.setCreatedAt(this.getCreatedAt());
//		audit.setCreatedBy(this.getCreatedBy());
//		audit.setUpdatedAt(this.getUpdatedAt());
//		audit.setUpdatedBy(this.getUpdatedBy());
//		audit.setXCol1(this.getXCol1());
//		audit.setXCol2(this.getXCol2());
//		audit.setXCol3(this.getXCol3());
//
//		return audit;
//	}
}
