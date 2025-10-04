package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.billing.dto.MilkSummaryDataEntry;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MilkCollectionSummaryImportTask extends Task<List<MilkSummaryDataEntry>> {

    private File file;
    private List<MilkType> milkTypeList;
    private List<Member> memberList;


    private static final Logger LOGGER = LoggerFactory.getLogger(MilkCollectionSummaryImportTask.class);

    public MilkCollectionSummaryImportTask(File file, List<MilkType> milkTypeList, List<Member> memberList) {
        this.file = file;
        this.milkTypeList = milkTypeList;
        this.memberList = memberList;
    }

    @Override
    protected List<MilkSummaryDataEntry> call() throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            Sheet dataSheet = workbook.getSheetAt(0); // use first sheet for milkCollection data
            Iterator<Row> iterator = dataSheet.iterator();
            boolean firstRow = true;
            List<MilkSummaryDataEntry> list = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                // Date
                Cell cellDate = row.getCell(0);
                LocalDate collectionDate = CommonUtils.excelDate(cellDate.getStringCellValue());
                if (collectionDate == null) {
                    continue;
                }

                // MemberCode
                Cell cellCode = row.getCell(1);
                String code = MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(cellCode.getStringCellValue());
                if (code.equalsIgnoreCase(MainApp.identityDto.getSociety().getCode() + "0000")) {
                    continue;
                }
                Member member = memberList.stream().filter(p -> code.equals(p.getCode())).findAny().orElse(null);
                if (member == null)
                    continue;

                // MilkType
                Cell cellMilkType = row.getCell(2);
                String milkTypeStr = cellMilkType.getStringCellValue();
                MilkType milkType = null;
                if (milkTypeStr == null || milkTypeStr.isEmpty())
                    milkType = milkTypeList.get(0);
                else {
                    milkType = milkTypeList.stream().filter(p -> milkTypeStr.equalsIgnoreCase(p.getName()) || milkTypeStr.charAt(0) == p.getName().charAt(0) || milkTypeStr.toLowerCase().charAt(0) == p.getName().toLowerCase().charAt(0))
                            .findAny().orElse(null);
                    if (milkType == null)
                        milkType = milkTypeList.get(0);
                }

                // Qty
                Cell cellFat = row.getCell(3);
                BigDecimal qty = BigDecimal.valueOf(cellFat.getNumericCellValue());
                if (qty == null) {
                    continue;
                }

                // Amt
                Cell cellSnf = row.getCell(4);
                BigDecimal amt = BigDecimal.valueOf(cellSnf.getNumericCellValue());
                if (amt == null) {
                    continue;
                }

                MilkSummaryDataEntry milkCollection = new MilkSummaryDataEntry();
                milkCollection.setDate(collectionDate);
                milkCollection.setMember(member);
                milkCollection.setMilkType(milkType);
                milkCollection.setMilkQuantity(qty);
                milkCollection.setMilkAmount(amt);
                milkCollection.setSociety(MainApp.identityDto.getSociety());
                milkCollection.setDock(MainApp.identityDto.getDock());
                milkCollection.setUnion(MainApp.identityDto.getUnion().getCode());

                list.add(milkCollection);
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("Collection summary Import Error", e);
        }
        return null;
    }
}
