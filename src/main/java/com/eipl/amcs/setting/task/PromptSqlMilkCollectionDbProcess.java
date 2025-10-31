package com.eipl.amcs.setting.task;

import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromptSqlMilkCollectionDbProcess extends Task<List<MilkCollectionMigration>> {
    private final String filePath;
    private final String dbName;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public PromptSqlMilkCollectionDbProcess(String filePath, String dbName, LocalDate fromDate, LocalDate toDate) {
        this.filePath = filePath;
        this.dbName = dbName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollectionMigration> call() throws Exception {
        List<MilkCollectionMigration> list = new ArrayList<>();
        try {

            String connectionUrl = "jdbc:sqlserver://IT40\\EIPL;databaseName=" + dbName + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true;user=sa;password=eipl";
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select cast(DATENAME(MM,Date)as varchar(3)) +'-'+ Cast(DATEPART(YYYY,Date) as varchar(4)) as month, count(*) as count from tblILedger" +
                        " where  format(Date, 'yyyy-MM-dd') >= '" + fromDate.toString() + "' AND  format(Date, 'yyyy-MM-dd') <= '" + toDate.toString() +
                        "' group by cast(DATENAME(MM,Date)as varchar(3)) +'-'+ Cast(DATEPART(YYYY,Date) as varchar(4))");

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
