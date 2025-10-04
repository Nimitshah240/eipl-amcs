package com.eipl.amcs.setting.task;

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
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HisaabMitraMilkCollectionImportTask extends Task<List<MilkCollection>> {

    private File file;
    private List<MilkType> milkTypeList;
    private List<Shift> shiftList;
    private List<Member> memberList;


    private static final Logger LOGGER = LoggerFactory.getLogger(HisaabMitraMilkCollectionImportTask.class);

    public HisaabMitraMilkCollectionImportTask(File file, List<MilkType> milkTypeList, List<Shift> shiftList, List<Member> memberList) {
        this.file = file;
        this.milkTypeList = milkTypeList;
        this.shiftList = shiftList;
        this.memberList = memberList;
    }


    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            List<Map<String, Object>> mapCollection = new ArrayList<>();
            final AtomicInteger count = new AtomicInteger(0);
            final AtomicInteger temp = new AtomicInteger(0);
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            Sheet dataSheet = workbook.getSheetAt(0); // use first sheet for milkCollection data
            Iterator<Row> iterator = dataSheet.iterator();
            boolean firstRow = true;
            int i = 1;
            while (iterator.hasNext()) {
                Map<String, Object> map = new HashMap<>();
                Row row = iterator.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                // sampleNo
                Cell cellSampleNo = row.getCell(0);
                Integer sampleNo = i;
                if (sampleNo == null) {
                    continue;
                }
                // Date
                Cell cellDate = row.getCell(0);


                DataFormatter formatter = new DataFormatter();
                String val = formatter.formatCellValue(dataSheet.getRow(i).getCell(0));
//                LocalDate collectionDate = CommonUtils.excelDate(val);
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
                LocalDate collectionDate = LocalDate.parse(val, formatters);
                if (collectionDate == null) {
                    continue;
                }

                // Shift
                Cell cellShift = row.getCell(1);
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
                map.put("collectiondate", CommonUtils.getLocalDateTimeFromDateAndShift(collectionDate, shift));
                map.put("shift", row.getCell(1).toString().equalsIgnoreCase("M") ? 1 : 2);


                // MemberCode
                String valCode = formatter.formatCellValue(dataSheet.getRow(i).getCell(2));
                map.put("membercode", MainApp.identityDto.getSociety().getCode() + String.format("%04d", Integer.parseInt(valCode)));


                // MilkType
                Cell cellMilkType = row.getCell(3);
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

                map.put("milktype", milkType.getCode());


                // Fat
                Cell cellFat = row.getCell(5);
                BigDecimal fat = BigDecimal.valueOf(cellFat.getNumericCellValue());
                if (fat == null) {
                    continue;
                }
                map.put("fat", new BigDecimal(5));


                // snf
                Cell cellSnf = row.getCell(14);
                BigDecimal snf = BigDecimal.valueOf(cellSnf.getNumericCellValue());
                if (snf == null) {
                    continue;
                }
                map.put("snf", new BigDecimal("0"));
                // qty
                Cell cellQty = row.getCell(4);
                BigDecimal qty = new BigDecimal(4);
                map.put("qty", qty);
//                BigDecimal qty = BigDecimal.valueOf(cellQty.getNumericCellValue());
                if (qty == null) {
                    continue;
                }

                // Amount
                Cell cellAmount = row.getCell(7);
                BigDecimal amt = new BigDecimal(7);
                map.put("amount", amt);
                BigDecimal amount = BigDecimal.valueOf(cellAmount.getNumericCellValue());
                if (amount == null) {
                    continue;
                }
                // Rate
                Cell cellRate = row.getCell(6);
                BigDecimal rate = BigDecimal.valueOf(cellRate.getNumericCellValue());
                if (rate == null) {
                    continue;
                }
                map.put("rate", amt.divide(qty, RoundingMode.HALF_DOWN));
                map.put("density", BigDecimal.ZERO);
                map.put("lactose", BigDecimal.ZERO);
                map.put("protein", BigDecimal.ZERO);
                map.put("clr", BigDecimal.ZERO);
                map.put("weightauto", false);
                map.put("qualityauto", false);
                map.put("avgparam", false);
                map.put("unioncode", MainApp.identityDto.getUnion().getCode());
                map.put("qtymode", CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0")));
                map.put("convqtymode", map.get("qtymode").toString().equalsIgnoreCase("1") ? 0 : 1);
                map.put("convqty", CommonUtils.convertQty(AppConstant.CollectionType.MEMBER_COLL, "4"));
                i += 1;
                map.put("sampleno", (i));
                map.put("code", MainApp.identityDto.getDock().getDockNo() + "-" + ((LocalDateTime) map.get("collectiondate")) + map.get("shift") + "-" + map.get("sampleno") + "-" + String.format("%04d", Integer.parseInt(valCode)));


                mapCollection.add(map);

                if (temp.incrementAndGet() >= AppConstant.MIGRATION_LIST_SIZE) {
                    processMilkCollection(mapCollection, count.incrementAndGet());
                    temp.set(0);
                    mapCollection.clear();
                }
            }
            processMilkCollection(mapCollection, count.incrementAndGet());
        } catch (Exception e) {
            LOGGER.error("Collection Import Error", e);
        }
        return null;
    }

    private void processMilkCollection(List<Map<String, Object>> listTemp, int current) {
        String mysqlUrl = "jdbc:mysql://127.0.0.1:3366/" + AppConstant.EIPL_DB_NAME;
        try (Connection connMySql = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            String sql = "INSERT INTO milk_collection(code,sample_no,collection_date,fat,snf,clr,water,density," +
                    "lectose,protein,rtpl,qty,amount,is_weight_auto,is_quality_auto,is_avg_param,union_code," +
                    "qty_mode,converted_qty,converted_qty_mode,member_code,shift_code,milk_type_code,milk_quality_type_code," +
                    "society_code,dock_no,created_at,created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            connMySql.setAutoCommit(false);
            PreparedStatement pstmt = connMySql.prepareStatement(sql);
            for (Map<String, Object> map : listTemp) {
                pstmt.setString(1, map.get("code").toString());
                pstmt.setInt(2, (int) map.get("sampleno"));
                pstmt.setObject(3, (LocalDateTime) map.get("collectiondate"));
                pstmt.setBigDecimal(4, (BigDecimal) map.get("fat"));
                pstmt.setBigDecimal(5, (BigDecimal) map.get("snf"));
                pstmt.setBigDecimal(6, BigDecimal.ZERO);
                pstmt.setBigDecimal(7, BigDecimal.ZERO);
                pstmt.setBigDecimal(8, (BigDecimal) map.get("density"));
                pstmt.setBigDecimal(9, (BigDecimal) map.get("lactose"));
                pstmt.setBigDecimal(10, (BigDecimal) map.get("protein"));
                pstmt.setBigDecimal(11, (BigDecimal) map.get("rate"));
                pstmt.setBigDecimal(12, (BigDecimal) map.get("qty"));
                pstmt.setBigDecimal(13, (BigDecimal) map.get("amount"));
                pstmt.setBoolean(14, (Boolean) map.get("weightauto"));
                pstmt.setBoolean(15, (Boolean) map.get("qualityauto"));
                pstmt.setBoolean(16, (Boolean) map.get("avgparam"));
                pstmt.setString(17, map.get("unioncode").toString());
                pstmt.setInt(18, (int) map.get("qtymode"));
                pstmt.setBigDecimal(19, (BigDecimal) map.get("convqty"));
                pstmt.setInt(20, (int) map.get("convqtymode"));
                pstmt.setString(21, map.get("membercode").toString());
                pstmt.setInt(22, (int) map.get("shift"));
                pstmt.setInt(23, (int) map.get("milktype"));
                pstmt.setInt(24, 1);
                pstmt.setString(25, MainApp.identityDto.getSociety().getCode());
                pstmt.setString(26, MainApp.identityDto.getDock().getDockNo());
                pstmt.setObject(27, LocalDateTime.now());
                pstmt.setString(28, "MIGR");

                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
            updateMessage("Migration in progress " + current);

            connMySql.commit();
            connMySql.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}