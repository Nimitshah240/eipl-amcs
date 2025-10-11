package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
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

public class SubLedgerOpeningBalanceImportTask extends Task<List<SubLedgerOpeningBalance>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerOpeningBalanceImportTask.class);
    private final File file;
    private final List<SubLedger> subledgerList;
    private final List<FinancialYear> financialYearList;

    public SubLedgerOpeningBalanceImportTask(File file, List<SubLedger> subledgerList, List<FinancialYear> financialYear) {
        this.file = file;
        this.subledgerList = subledgerList;
        this.financialYearList = financialYear;

    }

    @Override
    protected List<SubLedgerOpeningBalance> call() throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            DataFormatter formatter = new DataFormatter();
            Sheet dataSheet = workbook.getSheetAt(0); // use first sheet for member data
            Iterator<Row> iterator = dataSheet.iterator();
            boolean firstRow = true;
            List<SubLedgerOpeningBalance> list = new ArrayList<>();
            BigDecimal creditLimit = new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_CREDIT_LIMIT, "0"));
            int i = 1;
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                // Financial Year
//                Cell cellFinancialYear = row.getCell(0);
//                String financialYear = cellFinancialYear.getStringCellValue();
//                if (financialYear == null || financialYear.isEmpty()) {
//                    continue;
//                }

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

                //SubLedger
                Cell cellSubLedger = row.getCell(1);
                String subledgerStr = cellSubLedger.getStringCellValue();
                SubLedger subledger = null;
                if (subledgerStr == null || subledgerStr.isEmpty()) {
                    subledger = subledgerList.get(0);
                } else {
                    subledger = subledgerList.stream().filter(p -> subledgerStr.equalsIgnoreCase(p.getName()))
                            .findAny().orElse(null);
                    if (subledger == null)
                        subledger = subledgerList.get(0);
                }


                Cell cellCreditDebit = row.getCell(2);
                String creditDebit = cellCreditDebit.getStringCellValue();

//                Cell cellBalance= row.getCell(2);
//                String balance = cellBalance.getStringCellValue();
//                Cell cellCode = row.getCell(3);
                String balance = formatter.formatCellValue(dataSheet.getRow(i).getCell(3));


                SubLedgerOpeningBalance subLedgerOpeningBalance = new SubLedgerOpeningBalance();
                subLedgerOpeningBalance.setSociety(MainApp.identityDto.getSociety());
                subLedgerOpeningBalance.setUnionCode(MainApp.identityDto.getUnion().getCode());
                subLedgerOpeningBalance.setSubLedger(subledger);
                subLedgerOpeningBalance.setCreditDebit(Boolean.valueOf(creditDebit));
                subLedgerOpeningBalance.setFinancialYearsCode(financialYear1.getCode());
                subLedgerOpeningBalance.setBalance(new BigDecimal(balance));


                list.add(subLedgerOpeningBalance);
                i += 1;
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("SubLedgerOpeningBalance Import Error", e);
        }
        return null;
    }
}
