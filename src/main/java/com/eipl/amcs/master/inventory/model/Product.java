package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseModel {

    @Id
    private String code;
    private String name;
    private String nameLocal;
    private String referenceCode;
    @Digits(integer = 7, fraction = 3)
    private BigDecimal baseConversionFactor;
    @Digits(integer = 7, fraction = 3)
    private BigDecimal conversionFactor;
    private Short entryType;
    @Column(name = "is_indent")
    private Boolean indent;
    @Column(name = "is_saleable")
    private Boolean saleable;
    @Column(name = "is_milk")
    private boolean milk;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnitSerialize.class)
    @JsonDeserialize(using = UnitDeserializer.class)
    @JoinColumn(name = "conversion_unit_code", foreignKey = @ForeignKey(name = "fk_products_conversion_unit"))
    private Unit conversionUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnitSerialize.class)
    @JsonDeserialize(using = UnitDeserializer.class)
    @JoinColumn(name = "primary_uom_code", foreignKey = @ForeignKey(name = "fk_products_primary_uom"))
    private Unit primaryUom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductGroupSerialize.class)
    @JsonDeserialize(using = ProductGroupDeserializer.class)
    @JoinColumn(name = "product_group_code", foreignKey = @ForeignKey(name = "fk_products_product_group_code"))
    @JsonIgnoreProperties(value = {"unit"})
    private ProductGroup productGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(name = "fk_products_tax_code"))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "secondary_packaging_code", foreignKey = @ForeignKey(name = "fk_products_secondary_packaging"))
    private Product secondaryPackaging;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_products_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_products_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", referencedColumnName = "code")
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_state_tax_code", referencedColumnName = "code")
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JsonIgnoreProperties(value = {"union"})
    private Tax otherStateTax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "purchase_ledger_code", referencedColumnName = "code")
    private Ledger purchaseLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "stock_ledger_code", referencedColumnName = "code")
    private Ledger stockLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "local_sale_ledger_code", referencedColumnName = "code")
    private Ledger localSaleLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "coupon_ledger_code", referencedColumnName = "code")
    private Ledger couponLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "sale_ledger_code", referencedColumnName = "code")
    private Ledger saleLedger;

    public Product(String code, String name, String nameLocal) {
        this.code = code;
        this.name = name;
        this.nameLocal = nameLocal;
    }

    @Override
    public String getTableName() {
        return "products";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductAudit audit = new ProductAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setName(this.getName());
        audit.setNameLocal(this.getNameLocal());
        audit.setReferenceCode(this.getReferenceCode());
        audit.setBaseConversionFactor(this.getBaseConversionFactor());
        audit.setConversionFactor(this.getConversionFactor());
        audit.setIndent(this.getIndent());
        audit.setSaleable(this.getSaleable());
        audit.setConversionUnit(this.getConversionUnit());
        audit.setPrimaryUom(this.getPrimaryUom());
        audit.setProductGroup(this.getProductGroup());
        audit.setTax(this.getTax());
        audit.setUnion(this.getUnion());
        audit.setSociety(this.getSociety());
        audit.setSecondaryPackaging(this.getSecondaryPackaging());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setActive(this.isActive());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        audit.setMilkType(this.getMilkType());
        audit.setSaleLedger(this.getSaleLedger());
        audit.setCouponLedger(this.getCouponLedger());
        audit.setLocalSaleLedger(this.getLocalSaleLedger());
        audit.setPurchaseLedger(this.getPurchaseLedger());
        audit.setStockLedger(this.getStockLedger());
        audit.setMilk(this.isMilk());
        audit.setOtherStateTax(this.getOtherStateTax());

        audit.setOriginatingOrgCode(this.getOriginatingOrgCode());
        audit.setOriginatingOrgType(this.getOriginatingOrgType());
        audit.setOriginatingType(this.getOriginatingType());
        return audit;
    }

    @Override
    public String toString() {
        String refCode = this.referenceCode == null ? "" : this.referenceCode;
        return refCode + ' ' + CommonUtils.getLocalString(this.name, this.nameLocal);
    }

}
