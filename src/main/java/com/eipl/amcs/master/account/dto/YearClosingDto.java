package com.eipl.amcs.master.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eipl.amcs.master.account.model.SocietyYearClosing;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class YearClosingDto {
    private List<LedgerOpeningBalance> ledgerOpeningBalanceList;
    private List<SubLedgerOpeningBalance> subLedgerOpeningBalanceList;
    private SocietyYearClosing societyYearClosing;

//    public YearClosingDto() {
//    }
//
//    public YearClosingDto(List<LedgerOpeningBalance> ledgerOpeningBalanceList, List<SubLedgerOpeningBalance> subLedgerOpeningBalanceList, SocietyYearClosing societyYearClosing) {
//        this.ledgerOpeningBalanceList = ledgerOpeningBalanceList;
//        this.subLedgerOpeningBalanceList = subLedgerOpeningBalanceList;
//        this.societyYearClosing = societyYearClosing;
//    }
//
//    public List<LedgerOpeningBalance> getLedgerOpeningBalanceList() {
//        return ledgerOpeningBalanceList;
//    }
//
//    public void setLedgerOpeningBalanceList(List<LedgerOpeningBalance> ledgerOpeningBalanceList) {
//        this.ledgerOpeningBalanceList = ledgerOpeningBalanceList;
//    }
//
//    public List<SubLedgerOpeningBalance> getSubLedgerOpeningBalanceList() {
//        return subLedgerOpeningBalanceList;
//    }
//
//    public void setSubLedgerOpeningBalanceList(List<SubLedgerOpeningBalance> subLedgerOpeningBalanceList) {
//        this.subLedgerOpeningBalanceList = subLedgerOpeningBalanceList;
//    }
//
//    public SocietyYearClosing getSocietyYearClosing() {
//        return societyYearClosing;
//    }
//
//    public void setSocietyYearClosing(SocietyYearClosing societyYearClosing) {
//        this.societyYearClosing = societyYearClosing;
//    }
}
