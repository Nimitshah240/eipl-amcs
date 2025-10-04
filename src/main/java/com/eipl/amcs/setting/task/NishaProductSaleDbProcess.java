package com.eipl.amcs.setting.task;

import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NishaProductSaleDbProcess extends Task<List<ProductSale>> {
    private String filePath;
    private LocalDateTime saleDate;



    public NishaProductSaleDbProcess(String filePath, LocalDateTime saleDate) {
        this.filePath = filePath;
       this.saleDate=saleDate;

    }

    @Override
    protected List<ProductSale> call() throws Exception {
        List<ProductSale> list = new ArrayList<>();
        try {
            String urlDb = "jdbc:ucanaccess://" + filePath;

            try (Connection connection = DriverManager.getConnection(urlDb, "","Oracle8.0")) {
                Statement statement = connection.createStatement();
//
                ResultSet resultSet = statement.executeQuery("select * from Kapat");
                while (resultSet.next()) {
                    ProductSale migration = new ProductSale();

                    migration.setAmount(resultSet.getBigDecimal("Kapat1"));
                    migration.setAmount(resultSet.getBigDecimal("Payment"));



                   // migration.setSaleDate(resultSet.getDate("date"));


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
