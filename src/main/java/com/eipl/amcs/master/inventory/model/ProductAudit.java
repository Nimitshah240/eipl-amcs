package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "products_audit")
public class ProductAudit extends BaseModelAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "conversion_unit_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Unit conversionUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnitSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "primary_uom_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Unit primaryUom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductGroupSerialize.class)
    @JsonDeserialize(using = ProductGroupDeserializer.class)
    @JoinColumn(name = "product_group_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"unit"})
    private ProductGroup productGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "secondary_packaging_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Product secondaryPackaging;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_state_tax_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JsonIgnoreProperties(value = {"union"})
    private Tax otherStateTax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "purchase_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Ledger purchaseLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "stock_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Ledger stockLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "local_sale_ledger_code",  foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Ledger localSaleLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "sale_ledger_code",  foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Ledger saleLedger;

    @Override
    public String getTableName() {
        return "products_audit";
    }
}
