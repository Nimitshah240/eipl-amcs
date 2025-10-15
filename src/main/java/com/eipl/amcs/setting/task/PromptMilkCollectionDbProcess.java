package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromptMilkCollectionDbProcess extends Task<List<MilkCollectionMigration>> {
    private String filePath;

    private LocalDate fromDate;
    private LocalDate toDate;

    public PromptMilkCollectionDbProcess(String filePath, LocalDate fromDate, LocalDate toDate) {
        this.filePath = filePath;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollectionMigration> call() throws Exception {
        List<MilkCollectionMigration> list = new ArrayList<>();
        try {
            String urlDb = "jdbc:ucanaccess://" + filePath;

            try (Connection connection = DriverManager.getConnection(urlDb, "", AppConstant.PROMPT_DB_PASS)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select format(Date, 'mmm yyyy') as month, count(*) as count from tblILedger " +
                                "where  format(Date, 'yyyy-MM-dd') >= '" + fromDate.toString() + "' AND  format(Date, 'yyyy-MM-dd') <= '" + toDate.toString() +
                        "'group by format(Date, 'mmm yyyy')");

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
