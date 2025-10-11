package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.model.SocietyYearClosing;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class YearClosingDto {
    private List<LedgerOpeningBalance> ledgerOpeningBalanceList;
    private List<SubLedgerOpeningBalance> subLedgerOpeningBalanceList;
    private SocietyYearClosing societyYearClosing;
}
