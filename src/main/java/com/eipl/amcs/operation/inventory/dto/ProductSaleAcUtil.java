package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.master.account.model.Ledger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductSaleAcUtil {
    private Ledger ledger;
    private Ledger creditLedger;
    private BigDecimal amount;
    private String narration;
}
