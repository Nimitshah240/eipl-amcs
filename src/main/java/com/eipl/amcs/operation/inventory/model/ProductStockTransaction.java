package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "product_stock_transaction")
public class ProductStockTransaction extends BaseModelTxn {

    @Id
    @Size(max = 15)
    private String code;

    @Digits(integer = 7, fraction = 3)
    private BigDecimal finalValue;
    @Digits(integer = 7, fraction = 3)
    private BigDecimal newValue;
    @Digits(integer = 7, fraction = 3)
    private BigDecimal oldValue;
    @Size(max = 35)
    private String referenceCode;
    private LocalDate transactionDate;
    @Size(max = 20)
    private String transactionType;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_stock_transaction_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_stock_transaction_product_code"))
    private Product product;

    @Override
    public String getTableName() {
        return "product_stock_transaction";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

}
