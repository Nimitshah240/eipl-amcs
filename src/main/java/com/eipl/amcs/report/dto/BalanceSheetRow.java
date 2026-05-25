package com.eipl.amcs.report.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceSheetRow {
    private String liabilityCode;
    private String liabilityName;
    private Double liabilityBalance;

    private String assetCode;
    private String assetName;
    private Double assetBalance;

    // Constructor for a full row (both sides populated)
    public BalanceSheetRow(LedgerBalance liability, LedgerBalance asset) {
        if (liability != null) {
            this.liabilityCode = liability.getLedgerCode();
            this.liabilityName = liability.getLedgerName();
            this.liabilityBalance = liability.getBalance();
        }
        if (asset != null) {
            this.assetCode = asset.getLedgerCode();
            this.assetName = asset.getLedgerName();
            this.assetBalance = Math.abs(asset.getBalance());
        }
    }
}