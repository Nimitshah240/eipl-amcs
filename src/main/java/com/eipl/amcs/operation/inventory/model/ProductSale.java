package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.DockDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.json.serialize.DockSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "product_sale")
public class ProductSale extends BaseModelTxn {
    @Id
    @Size(max = 35)
    private String invoiceNo;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal amount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal discount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal netAmount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal taxAmount;

    private LocalDate invoiceDate;
    private LocalDate deductionStartDate;
    private String voucherNo;
    private Short noOfInstallments;
    private Short paymentMode; //0-cash, 1-credit
    private Short consumerType;
    private Short transactionType;
    @Size(max = 15)
    private String consumerCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DockSerialize.class)
    @JsonDeserialize(using = DockDeserializer.class)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(name = "fk_product_sale_dock_no"))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_product_sale_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_sale_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "product_sale";
    }

    @Override
    public Object getId() {
        return this.getInvoiceNo();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductSaleAudit audit = new ProductSaleAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setInvoiceNo(this.getInvoiceNo());
        audit.setAmount(this.getAmount());
        audit.setDiscount(this.getDiscount());
        audit.setNetAmount(this.getNetAmount());
        audit.setTaxAmount(this.getTaxAmount());
        audit.setInvoiceDate(this.getInvoiceDate());
        audit.setDeductionStartDate(this.getDeductionStartDate());
        audit.setVoucherNo(this.getVoucherNo());
        audit.setNoOfInstallments(this.getNoOfInstallments());
        audit.setPaymentMode(this.getPaymentMode());
        audit.setConsumerType(this.getConsumerType());
        audit.setTransactionType(this.getTransactionType());
        audit.setConsumerCode(this.getConsumerCode());
        audit.setDock(this.getDock());
        audit.setUnion(this.getUnion());
        audit.setSociety(this.getSociety());

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
