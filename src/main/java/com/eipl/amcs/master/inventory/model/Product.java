package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
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
@Table(name = "products")
public class Product extends BaseModel {

    @Id
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
    @JoinColumn(name = "conversion_unit_code", foreignKey = @ForeignKey(name = "fk_products_conversion_unit"))
    private Unit conversionUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_uom_code", foreignKey = @ForeignKey(name = "fk_products_primary_uom"))
    private Unit primaryUom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_group_code", foreignKey = @ForeignKey(name = "fk_products_product_group_code"))
    @JsonIgnoreProperties(value = {"unit"})
    private ProductGroup productGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(name = "fk_products_tax_code"))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_packaging_code", foreignKey = @ForeignKey(name = "fk_products_secondary_packaging"))
    private Product secondaryPackaging;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_products_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_products_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

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

        return audit;
    }

    public Product(String code, String name, String nameLocal) {
        this.code = code;
        this.name = name;
        this.nameLocal = nameLocal;
    }
}
