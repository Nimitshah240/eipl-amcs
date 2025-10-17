package com.eipl.amcs.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LedgerClose {
    private String ledgerCode;
    private String ledgerName;
    private boolean creditDebit;
    private double balance;

//    public LedgerClose(String ledgerCode, String ledgerName, boolean creditDebit, double balance) {
//        this.ledgerCode = ledgerCode;
//        this.ledgerName = ledgerName;
//        this.creditDebit = creditDebit;
//        this.balance = balance;
//    }
//
//
//    public LedgerClose() {
//    }
//
//
//    public String getLedgerCode() {
//        return ledgerCode;
//    }
//
//    public void setLedgerCode(String ledgerCode) {
//        this.ledgerCode = ledgerCode;
//    }
//
//    public String getLedgerName() {
//        return ledgerName;
//    }
//
//    public void setLedgerName(String ledgerName) {
//        this.ledgerName = ledgerName;
//    }
//
//    public boolean isCreditDebit() {
//        return creditDebit;
//    }
//
//    public void setCreditDebit(boolean creditDebit) {
//        this.creditDebit = creditDebit;
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(double balance) {
//        this.balance = balance;
//    }


}
