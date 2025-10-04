package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EMandaliMilkCollectionDbSaveTask extends Task<Boolean> {
    private List<MilkType> milkTypeList;
    private List<Shift> shiftList;
    private String cowRange;
    private String buffRange;
    private String dbName;
    private LocalDate fromDate;
    private LocalDate toDate;

    public EMandaliMilkCollectionDbSaveTask(List<MilkType> milkTypeList, List<Shift> shiftList,
                                            String cowRange, String buffRange,String dbName,LocalDate fromDate,LocalDate toDate) {
        this.milkTypeList = milkTypeList;
        this.shiftList = shiftList;
        this.cowRange = cowRange;
        this.buffRange = buffRange;
        this.dbName = dbName;
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    @Override
    protected Boolean call() throws Exception {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=sa;password=everest;integretedSecurity=false";
            Map<String, Integer> mapShift = new HashMap<>();
            for (Shift shift : shiftList) {
                mapShift.put(shift.getName().substring(0, 1).toUpperCase(), shift.getCode());
            }
            Map<String, Integer> mapMilkType = new HashMap<>();
            for (MilkType milkType : milkTypeList) {
                mapMilkType.put(milkType.getName().toUpperCase().substring(0, 1), milkType.getCode());
            }

            String[] cowRangeArr = cowRange.split("-");
            int cowMin = CommonUtils.strToInteger(cowRangeArr[0]);
            int cowMax = CommonUtils.strToInteger(cowRangeArr[1]);
            String[] buffRangeArr = buffRange.split("-");
            int buffMin = CommonUtils.strToInteger(buffRangeArr[0]);
            int buffMax = CommonUtils.strToInteger(buffRangeArr[1]);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");

            LocalTime morningTime = LocalTime.of(6, 0);
            LocalTime eveningTime = LocalTime.of(18, 0);
            try (Connection connection = DriverManager.getConnection(connectionUrl); Statement stmt = connection.createStatement();) {

//            try (Connection connection = DriverManager.getConnection(connectionUrl, "", AppConstant.PROMPT_DB_PASS)) {
                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("select cast(DATENAME(MM,dtDate)as varchar(3)) +'-'+ " +
//                        "Cast(DATEPART(YYYY,dtDate) as varchar(4)) as month, count(*) as count from farmercollection" +
//                        " where  format(dtDate, 'yyyy-MM-dd') >= '" + fromDate.toString() + "' AND  format(dtDate, 'yyyy-MM-dd') <= '" + toDate.toString() +
//                        "' group by cast(DATENAME(MM,dtDate)as varchar(3)) +'-'+ Cast(DATEPART(YYYY,dtDate) as varchar(4))");

                ResultSet resultSet = statement.executeQuery("select cast(DATENAME(MM,dtDate)as varchar(3)) +'-'+ " +
                        "Cast(DATEPART(YYYY,dtDate) as varchar(4)) as month, count(*) as count from farmercollection" +
                        " where CONVERT(VARCHAR,dtDate,23) >= '" + fromDate.toString() + "' AND CONVERT(VARCHAR,dtDate,23) <= '" + toDate.toString() +
                        "' group by cast(DATENAME(MM,dtDate)as varchar(3)) +'-'+ Cast(DATEPART(YYYY,dtDate) as varchar(4))");
                List<String> listMonth = new ArrayList<>();
                while (resultSet.next()) {
                    listMonth.add(resultSet.getString("month"));
                }
                resultSet.close();
                statement.close();

                // select data
                for (String month : listMonth) {
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("select * from farmercollection where CONVERT(VARCHAR,dtDate,23) >= '" +
                            fromDate.toString() + "' AND CONVERT(VARCHAR,dtDate,23) <= '" + toDate.toString() + "'");
                    List<Map<String, Object>> mapCollection = new ArrayList<>();
                    String shift = null;
                    while (resultSet.next()) {
                        if (resultSet.getString("farmerid") == null ||
                                resultSet.getString("farmerid").isEmpty() ||
                                resultSet.getString("farmerid").equalsIgnoreCase("0000") ||
                                resultSet.getDouble("Qty") == 0)
                            continue;
                        Map<String, Object> map = new HashMap<>();
                        int codeEx = CommonUtils.strToInteger(resultSet.getString("farmerid"));
                        map.put("membercode", MainApp.identityDto.getSociety().getCode() + String.format("%04d", codeEx));
                        map.put("fat", new BigDecimal(resultSet.getString("fat")));
                        map.put("snf", new BigDecimal(resultSet.getString("snf")));
                        map.put("amount", new BigDecimal(resultSet.getString("amount")));
                        map.put("rate", new BigDecimal(resultSet.getString("rtpl")));
                        map.put("qty", new BigDecimal(resultSet.getString("qty")));
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
                        map.put("convqty", CommonUtils.convertQty(AppConstant.CollectionType.MEMBER_COLL, resultSet.getString("qty")));
                        shift = resultSet.getString("Shift");
                        LocalDate date = resultSet.getDate("dtDate").toLocalDate();
                        if (shift == null || shift.isEmpty() || shift.equalsIgnoreCase("M")) {
                            map.put("shift", mapShift.get("M"));
                            map.put("collectiondate", LocalDateTime.of(date, morningTime));
                        } else {
                            map.put("shift", mapShift.get(shift.toUpperCase()));
                            map.put("collectiondate", LocalDateTime.of(date, eveningTime));
                        }
                        if (codeEx >= cowMin && codeEx <= cowMax)
                            map.put("milktype", mapMilkType.get("C"));
                        else if (codeEx >= buffMin && codeEx <= buffMax)
                            map.put("milktype", mapMilkType.get("B"));
                        else
                            map.put("milktype", mapMilkType.get("C"));
                        map.put("sampleno", resultSet.getInt("sampleno"));
                        map.put("code", MainApp.identityDto.getDock().getDockNo() + "-" + ((LocalDateTime) map.get("collectiondate")).format(dateTimeFormatter) + map.get("shift") + "-" + map.get("sampleno")+"-"+codeEx);

                        mapCollection.add(map);
                    }
                    resultSet.close();
                    statement.close();

                    // split map and save in mysql
                    List<List<Map<String, Object>>> listTemp = ListUtils.partition(mapCollection, AppConstant.MIGRATION_LIST_SIZE);
                    String mysqlUrl = "jdbc:mysql://localhost:3366/" + AppConstant.EIPL_DB_NAME;
                    try (Connection connMySql = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
                        String sql = "INSERT INTO milk_collection(code,sample_no,collection_date,fat,snf,clr,water,density," +
                                "lectose,protein,rtpl,qty,amount,is_weight_auto,is_quality_auto,is_avg_param,union_code," +
                                "qty_mode,converted_qty,converted_qty_mode,member_code,shift_code,milk_type_code,milk_quality_type_code," +
                                "society_code,dock_no,created_at,created_by) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        int current = 1;
                        connMySql.setAutoCommit(false);
                        for (List<Map<String, Object>> maps : listTemp) {
                            PreparedStatement pstmt = connMySql.prepareStatement(sql);
                            for (Map<String, Object> map : maps) {
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
                            updateMessage("Migration in progress " + current + " of " + listTemp.size());
                            current++;
                        }
                        connMySql.commit();
                        connMySql.setAutoCommit(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
