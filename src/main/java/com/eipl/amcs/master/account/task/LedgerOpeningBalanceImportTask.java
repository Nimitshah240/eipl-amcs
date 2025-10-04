package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LedgerOpeningBalanceImportTask extends Task<List<LedgerOpeningBalance>> {

    private File file;

    private List<Ledger> ledgerList;
    private List<FinancialYear> financialYearList;

    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerOpeningBalanceImportTask.class);

    public LedgerOpeningBalanceImportTask(File file, List<Ledger> ledgerList, List<FinancialYear> financialYear) {
        this.file = file;
        this.ledgerList = ledgerList;
        this.financialYearList = financialYear;

    }

    @Override
    protected List<LedgerOpeningBalance> call() throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            DataFormatter formatter = new DataFormatter();
            Sheet dataSheet = workbook.getSheetAt(0); // use first sheet for member data
            Iterator<Row> iterator = dataSheet.iterator();
            boolean firstRow = true;
            List<LedgerOpeningBalance> list = new ArrayList<>();
            BigDecimal creditLimit = new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_CREDIT_LIMIT, "0"));
            int i = 1;
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                Cell cellFinancialYear = row.getCell(0);
                String financialYearStr = cellFinancialYear.getStringCellValue();
                FinancialYear financialYear1 = null;
                if (financialYearStr == null || financialYearStr.isEmpty())
                    financialYear1 = financialYearList.get(0);
                else {
                    financialYear1 = financialYearList.stream().filter(p -> financialYearStr.equalsIgnoreCase(p.toString()))
                            .findAny().orElse(null);
                    if (financialYear1 == null)
                        financialYear1 = financialYearList.get(0);
                }

                //Ledger
                Cell cellLedger = row.getCell(1);
                String ledgerStr = cellLedger.getStringCellValue();
                Ledger ledger = null;
                if (ledgerStr == null || ledgerStr.isEmpty()) {
                    ledger = ledgerList.get(0);
                } else {
                    ledger = ledgerList.stream().filter(p -> ledgerStr.equalsIgnoreCase(p.getName()))
                            .findAny().orElse(null);
                    if (ledger == null)
                        ledger = ledgerList.get(0);
                }



                Cell cellCreditDebit= row.getCell(2);
                String creditDebit = cellCreditDebit.getStringCellValue();

                String balance = formatter.formatCellValue(dataSheet.getRow(i).getCell(3));

                LedgerOpeningBalance ledgerOpeningBalance = new LedgerOpeningBalance();
                ledgerOpeningBalance.setSociety(MainApp.identityDto.getSociety());
                ledgerOpeningBalance.setUnionCode(MainApp.identityDto.getUnion().getCode());
                ledgerOpeningBalance.setLedger(ledger);
                ledgerOpeningBalance.setCreditDebit(Boolean.valueOf(creditDebit));
                ledgerOpeningBalance.setFinancialYearsCode(financialYear1.getCode());
                ledgerOpeningBalance.setBalance(new BigDecimal(balance));


                list.add(ledgerOpeningBalance);
                i+=1;
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("LedgerOpeningBalance Import Error", e);
        }
        return null;
    }
}
