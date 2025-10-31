package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.ProductDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.ProductSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
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
@Table(name = "product_stock")
public class ProductStock extends BaseModelTxn {

    @Id
    private String code;
    @Digits(integer = 7, fraction = 3)
    private BigDecimal stock;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_stock_product_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_stock_society_code"))
    @JsonIgnoreProperties(value = {"branch", "union", "mcc", "bmc", "route", "state", "district", "subDistrict",
            "village", "hamlet", "bank", "plant"})
    private Society society;

    @Override
    public String getTableName() {
        return "product_stock";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductStockAudit audit = new ProductStockAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setStock(this.getStock());
        audit.setUnionCode(this.getUnionCode());
        audit.setProduct(this.getProduct());
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