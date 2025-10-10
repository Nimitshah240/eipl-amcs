package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
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
    @Size(max = 25)
    private String code;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;
    @Size(max = 25)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversion_unit_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Unit conversionUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_uom_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Unit primaryUom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_group_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"unit"})
    private ProductGroup productGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_packaging_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Product secondaryPackaging;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "products_audit";
    }
}
