package com.eipl.amcs.setting.task;

import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class FriendsMilkCollectionDbProcess extends Task<List<MilkCollectionMigration>> {
    private String filePath;
    private LocalDate fromDate;
    private LocalDate toDate;

    public FriendsMilkCollectionDbProcess(String filePath, LocalDate fromDate, LocalDate toDate) {
        this.filePath = filePath;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollectionMigration> call() throws Exception {
        List<MilkCollectionMigration> list = new ArrayList<>();
        try {
            String urlDb = "jdbc:ucanaccess://" + filePath;

            try (Connection connection = DriverManager.getConnection(urlDb, "","")) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select format(Dump_Date, 'mmm yyyy') as month, count(*) as count from dockside " +
                        "where  format(Dump_Date, 'yyyy-MM-dd') >= '" + fromDate.toString() + "' AND  format(Dump_Date, 'yyyy-MM-dd') <= '" + toDate.toString() + "' group by format(Dump_Date, 'mmm yyyy')");

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
