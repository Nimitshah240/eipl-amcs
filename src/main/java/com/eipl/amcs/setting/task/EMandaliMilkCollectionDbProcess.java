package com.eipl.amcs.setting.task;

import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EMandaliMilkCollectionDbProcess extends Task<List<MilkCollectionMigration>> {
    private String filePath;
    private String dbName;
    private LocalDate fromDate;
    private LocalDate toDate;

    public EMandaliMilkCollectionDbProcess(String filePath, String dbName,LocalDate fromDate, LocalDate toDate) {
        this.filePath = filePath;
        this.dbName = dbName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollectionMigration> call() throws Exception {
        List<MilkCollectionMigration> list = new ArrayList<>();
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=sa;password=everest;integretedSecurity=false";

//            String urlDb = "jdbc:ucanaccess://" + filePath;
            try (Connection connection = DriverManager.getConnection(connectionUrl); Statement stmt = connection.createStatement();) {

//            try (Connection connection = DriverManager.getConnection(urlDb, "", AppConstant.PROMPT_DB_PASS)) {



                Statement statement = connection.createStatement();

                String a = ("select    CAST(LEFT(DATENAME(mm,dtdate),3)as VARCHAR(3)) +'-'+ CAST(DATEPART(YYYY,dtdate) as VARCHAR(4)) as [Month]"+
                        ",count(*) as count"+
                        " from farmercollection "+
                        "where cast(dtdate as DATE)>= '"+ fromDate.toString() +"' and cast(dtdate as DATE) <='"+toDate.toString()+
                        "' GROUP BY CAST(LEFT(DATENAME(mm,dtdate),3)as VARCHAR(3)) +'-'+ CAST(DATEPART(YYYY,dtdate) as VARCHAR(4))");

                ResultSet resultSet = statement.executeQuery("select    CAST(LEFT(DATENAME(mm,dtdate),3)as VARCHAR(3)) +'-'+ CAST(DATEPART(YYYY,dtdate) as VARCHAR(4)) as [Month]"+
                        ",count(*) as count"+
                        " from farmercollection "+
                        "where cast(dtdate as DATE)>= '"+ fromDate.toString() +"' and cast(dtdate as DATE) <='"+toDate.toString()+
                        "' GROUP BY CAST(LEFT(DATENAME(mm,dtdate),3)as VARCHAR(3)) +'-'+ CAST(DATEPART(YYYY,dtdate) as VARCHAR(4))");





//                ResultSet resultSet = statement.executeQuery("select cast(DATENAME(MM,dtDate)as varchar(3)) +'-'+ Cast(DATEPART(YYYY,dtDate) as varchar(4)) as month, count(*) as count from farmercollection" +
//                        " where  format(dtDate, 'yyyy-MM-dd') >= .'" + fromDate.toString() + "' AND  format(dtDate, 'yyyy-MM-dd') <= '" + toDate.toString() +
//                        "' group by cast(DATENAME(MM,dtDate)as varchar(3)) +'-'+ Cast(DATEPART(YYYY,dtDate) as varchar(4))");

                while (resultSet.next()) {
                    MilkCollectionMigration migration = new MilkCollectionMigration();
                    migration.setMonth(resultSet.getString("month"));
                    migration.setCount(resultSet.getLong("count"));
                    list.add(migration);
                }
                resultSet.close();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
