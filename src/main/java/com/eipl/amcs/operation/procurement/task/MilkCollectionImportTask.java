package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MilkCollectionImportTask extends Task<List<MilkCollection>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkCollectionImportTask.class);
    private final File file;
    private final List<MilkType> milkTypeList;
    private final List<Shift> shiftList;
    private final List<Member> memberList;

    public MilkCollectionImportTask(File file, List<MilkType> milkTypeList, List<Shift> shiftList, List<Member> memberList) {
        this.file = file;
        this.milkTypeList = milkTypeList;
        this.shiftList = shiftList;
        this.memberList = memberList;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            Sheet dataSheet = workbook.getSheetAt(0); // use first sheet for milkCollection data
            Iterator<Row> iterator = dataSheet.iterator();
            boolean firstRow = true;
            List<MilkCollection> list = new ArrayList<>();
            int i = 1;
            while (iterator.hasNext()) {

                Row row = iterator.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                // sampleNo
                Cell cellSampleNo = row.getCell(0);
                Integer sampleNo = (int) cellSampleNo.getNumericCellValue();
                if (sampleNo == null) {
                    continue;
                }
                // Date
                Cell cellDate = row.getCell(1);


                DataFormatter formatter = new DataFormatter();
                String val = formatter.formatCellValue(dataSheet.getRow(i).getCell(1));
//                LocalDate collectionDate = CommonUtils.excelDate(val);
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate collectionDate = LocalDate.parse(val, formatters);
                if (collectionDate == null) {
                    continue;
                }

                // Shift
                Cell cellShift = row.getCell(2);
                String shiftStr = cellShift.getStringCellValue().toLowerCase();
                Shift shift = null;
                if (shiftStr == null || shiftStr.isEmpty())
                    shift = shiftList.get(0);
                else {
                    shift = shiftList.stream().filter(p -> shiftStr.equalsIgnoreCase(p.getName()) || shiftStr.charAt(0) == p.getName().toLowerCase().charAt(0))
                            .findAny().orElse(null);
                    if (shift == null)
                        shift = shiftList.get(0);
                }


                // MemberCode


                String valCode = formatter.formatCellValue(dataSheet.getRow(i).getCell(3));
                Cell cellCode = row.getCell(3);
                String code = MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(valCode);
                if (code.equalsIgnoreCase(MainApp.identityDto.getSociety().getCode() + "0000")) {
                    continue;
                }
                Member member = memberList.stream().filter(p -> code.equals(p.getCode())).findAny().orElse(null);
                if (member == null)
                    continue;

                // MilkType
                Cell cellMilkType = row.getCell(4);
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


                // Fat
                Cell cellFat = row.getCell(5);
                BigDecimal fat = BigDecimal.valueOf(cellFat.getNumericCellValue());
                if (fat == null) {
                    continue;
                }
                // snf
                Cell cellSnf = row.getCell(6);
                BigDecimal snf = BigDecimal.valueOf(cellSnf.getNumericCellValue());
                if (snf == null) {
                    continue;
                }
                // qty
                Cell cellQty = row.getCell(7);
                BigDecimal qty = BigDecimal.valueOf(cellQty.getNumericCellValue());
                if (qty == null) {
                    continue;
                }
                // Rate
                Cell cellRate = row.getCell(8);
                BigDecimal rate = BigDecimal.valueOf(cellRate.getNumericCellValue());
                if (rate == null) {
                    continue;
                }
                // Amount
                Cell cellAmount = row.getCell(9);
                BigDecimal amount = BigDecimal.valueOf(cellAmount.getNumericCellValue());
                if (amount == null) {
                    continue;
                }

                MilkCollection milkCollection = new MilkCollection();
                milkCollection.setSampleNo(sampleNo);
//                milkCollection.setCode("1023");
                milkCollection.setCollectionDate(CommonUtils.getLocalDateTimeFromDateAndShift(collectionDate, shift));
                milkCollection.setShift(shift);
                milkCollection.setMember(member);
                milkCollection.setMilkType(milkType);
                milkCollection.setFat(fat);
                milkCollection.setRtpl(rate);
                milkCollection.setQty(qty);
                milkCollection.setSnf(snf);
                milkCollection.setAmount(amount);

                milkCollection.setDensity(new BigDecimal("0"));
                milkCollection.setLectose(new BigDecimal("0"));
                milkCollection.setProtein(new BigDecimal("0"));

                milkCollection.setWeightAuto(false);
                milkCollection.setWeightAt(LocalDateTime.now());
                milkCollection.setQualityAuto(false);
                milkCollection.setQualityAt(LocalDateTime.now());
                milkCollection.setAvgParam(false);

                milkCollection.setWsCode(null);
                milkCollection.setAnalyserCode(null);

                milkCollection.setUnionCode(MainApp.identityDto.getUnion().getCode());
                milkCollection.setQtyMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0")));
                milkCollection.setConvertedQtyMode(milkCollection.getQtyMode() == 0 ? 1 : 0);
                milkCollection.setSociety(MainApp.identityDto.getSociety());
                milkCollection.setDock(MainApp.identityDto.getDock());

                list.add(milkCollection);
                i += 1;
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("Collection Import Error", e);
        }
        return null;
    }
}