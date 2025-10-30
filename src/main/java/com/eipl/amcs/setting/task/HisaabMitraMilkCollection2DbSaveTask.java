package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class HisaabMitraMilkCollection2DbSaveTask extends Task<Boolean> {
    private final List<MilkType> milkTypeList;
    private final List<Shift> shiftList;
    private final String filePath;
    private final String cowRange;
    private final String buffRange;

    public HisaabMitraMilkCollection2DbSaveTask(List<MilkType> milkTypeList, List<Shift> shiftList, String filePath,
                                                String cowRange, String buffRange) {
        this.milkTypeList = milkTypeList;
        this.shiftList = shiftList;
        this.filePath = filePath;
        this.cowRange = cowRange;
        this.buffRange = buffRange;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            String[] cowRangeArr = cowRange.split("-");
            int cowMin = CommonUtils.strToInteger(cowRangeArr[0]);
            int cowMax = CommonUtils.strToInteger(cowRangeArr[1]);
            String[] buffRangeArr = buffRange.split("-");
            int buffMin = CommonUtils.strToInteger(buffRangeArr[0]);
            int buffMax = CommonUtils.strToInteger(buffRangeArr[1]);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

            LocalTime morningTime = LocalTime.of(6, 0);
            LocalTime eveningTime = LocalTime.of(18, 0);
            Map<String, Integer> mapShift = new HashMap<>();
            for (Shift shift : shiftList) {
                mapShift.put(shift.getName().substring(0, 1).toUpperCase(), shift.getCode());
                mapShift.put(shift.getCode().toString(), shift.getCode());
            }
            Map<String, Integer> mapMilkType = new HashMap<>();
            for (MilkType milkType : milkTypeList) {
                mapMilkType.put(milkType.getName().toUpperCase().substring(0, 1), milkType.getCode());
            }
            final AtomicInteger count = new AtomicInteger(0);
            final AtomicInteger temp = new AtomicInteger(0);
            final AtomicInteger sampleNo = new AtomicInteger(0);
            List<Map<String, Object>> mapCollection = new ArrayList<>();
            AtomicInteger lineno = new AtomicInteger(0);
            AtomicReference<LocalDateTime> prevDate = new AtomicReference<>();
            LocalDateTime dateTime = null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));
            try (Stream<String> lines = Files.lines(new File(filePath).toPath(), Charset.defaultCharset())) {
                reader.lines().forEach(line -> {

                    if (line.charAt(0) == '/' || line.charAt(0) == ' ') {

                    } else {


                        Map<String, Object> map = new HashMap<>();
                        String[] arr = line.split(",");
                        int codeEx = CommonUtils.strToInteger(arr[2].replace("\"", ""));
                        map.put("membercode", MainApp.identityDto.getSociety().getCode() + String.format("%04d", codeEx));
                        map.put("fat", new BigDecimal(arr[5]));
                        map.put("snf", new BigDecimal("0"));
                        BigDecimal qty = new BigDecimal(arr[4]);
                        map.put("qty", qty);
                        BigDecimal amt = new BigDecimal(arr[7]);
                        map.put("amount", amt);
                        try {
                            map.put("rate", amt.divide(qty, RoundingMode.HALF_DOWN));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        map.put("convqty", CommonUtils.convertQty(AppConstant.CollectionType.MEMBER_COLL, arr[4]));
                        LocalDate date = LocalDate.now();
                        try {
                            date = LocalDate.parse(arr[0], formatter);
                        } catch (Exception e) {

                        }
                        if (arr[1] == null || arr[1].isEmpty() || arr[1].replaceAll("\"", "").equalsIgnoreCase("M") || arr[1].equalsIgnoreCase("1")) {
                            map.put("shift", mapShift.get("M"));
                            map.put("collectiondate", LocalDateTime.of(date, morningTime));

                        } else {
                            map.put("shift", mapShift.get("E"));
                            map.put("collectiondate", LocalDateTime.of(date, eveningTime));
                        }
                        if (codeEx >= cowMin && codeEx <= cowMax)
                            map.put("milktype", mapMilkType.get("C"));
                        else if (codeEx >= buffMin && codeEx <= buffMax)
                            map.put("milktype", mapMilkType.get("B"));
                        else
                            map.put("milktype", mapMilkType.get("C"));
                        map.put("sampleno", sampleNo.incrementAndGet());

                        map.put("code", MainApp.identityDto.getDock().getDockNo() + "-" + ((LocalDateTime) map.get("collectiondate")).format(dateTimeFormatter) + map.get("shift") + "-" + map.get("sampleno") + "-" + arr[2].replace("\"", ""));

                        try {
                            if (prevDate != null && !prevDate.get().isEqual((LocalDateTime) map.get("collectiondate"))) {
                                sampleNo.set(0);
                                prevDate.set((LocalDateTime) map.get("collectiondate"));
                            } else if (prevDate.get() == map.get("collectiondate")) {
                                sampleNo.incrementAndGet();
                            }
                        } catch (Exception eee) {
                            sampleNo.set(0);
                            prevDate.set((LocalDateTime) map.get("collectiondate"));
                        }


                        mapCollection.add(map);

                        if (temp.incrementAndGet() >= AppConstant.MIGRATION_LIST_SIZE) {
                            processMilkCollection(mapCollection, count.incrementAndGet());
                            temp.set(0);
                            mapCollection.clear();
                        }
                    }

                });
            }
            processMilkCollection(mapCollection, count.incrementAndGet());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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
                pstmt.setObject(3, map.get("collectiondate"));
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
