package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.master.account.model.Ledger;

import java.math.BigDecimal;

public class ProductSaleAcUtil {
    private Ledger ledger;
    private BigDecimal amount;
    private String narration;

    public ProductSaleAcUtil() {
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}
