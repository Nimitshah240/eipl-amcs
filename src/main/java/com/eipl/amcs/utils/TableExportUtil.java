package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TableExportUtil {


    public static boolean exportDataFromTableView(TableView<?> tableView, String tableName, String clazzName) {
        try {

            // check for setting
            List<TableColItem> columns = MainApp.tableConfiguration.containsKey(clazzName)
                    ? MainApp.tableConfiguration.get(clazzName)
                    : null;
            List<Integer> blankColumns = new ArrayList<>();
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Data");
            fileDialog.getExtensionFilters().addAll(new ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            fileDialog.setInitialFileName(tableName + ".xls");
            File file = fileDialog.showSaveDialog(MainApp.stage);
            file.renameTo(new File(file.getName() + ".xls"));
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFCellStyle styleFontBold = wb.createCellStyle();
                HSSFFont font = wb.createFont();
                font.setBold(true);
                styleFontBold.setFont(font);

                HSSFSheet sheet = wb.createSheet("DataSheet");

                // Create header column
                HSSFRow row = sheet.createRow(0);
                int cellValueHeading = 0;
                for (int cno = 0; cno < tableView.getColumns().size(); cno++) {
                    if (columns != null && checkColumn(tableView, cno, columns)) {
                        blankColumns.add(cno);
                        continue;
                    }
                    HSSFCell cell = row.createCell(cellValueHeading);
                    cell.setCellStyle(styleFontBold);
                    cell.setCellValue(tableView.getColumns().get(cno).getText());
                    cellValueHeading++;
                }

                HSSFCell cell = null;
                for (int i = 0; i < tableView.getItems().size(); i++) {
                    row = sheet.createRow(i + 1);
                    int colIndex = 0;
                    for (int j = 0; j < tableView.getColumns().size(); j++) {
                        if (columns != null && checkColumn(tableView, j, columns))
                            continue;
                        cell = row.createCell(colIndex++);
                        if (tableView.getColumns().get(j).getId() != null
                                && tableView.getColumns().get(j).getId().toLowerCase().contains("date")
                                && tableView.getColumns().get(j).getCellData(i) != null
                                && !tableView.getColumns().get(j).getCellData(i).equals(MainApp.bundle.getString("na")))
                            cell.setCellValue(tableView.getColumns().get(j).getCellData(i) != null
                                    ? LocalDate.parse(tableView.getColumns().get(j).getCellData(i).toString()).format(
                                    MainApp.DATE_FORMATTER)
                                    : "");
                        else
                            cell.setCellValue(tableView.getColumns().get(j).getCellData(i) != null
                                    ? String.valueOf(tableView.getColumns().get(j).getCellData(i))
                                    : "");
                    }
                }

                try {
                    wb.close();
                } catch (IOException e1) {

                    return false;
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    wb.write(out);
                    out.flush();
                    out.close();
                } catch (Exception e) {

                    return false;
                }

                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }


    public static boolean exportDataFrom2TableView(TableView<?> tableView, TableView<?> tableView2, String tableName, String clazzName) {
        try {

            // check for setting
            List<TableColItem> columns = MainApp.tableConfiguration.containsKey(clazzName)
                    ? MainApp.tableConfiguration.get(clazzName)
                    : null;
            List<Integer> blankColumns = new ArrayList<>();
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Data");
            fileDialog.getExtensionFilters().addAll(new ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            fileDialog.setInitialFileName(tableName + ".xls");
            File file = fileDialog.showSaveDialog(MainApp.stage);
            file.renameTo(new File(file.getName() + ".xls"));
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFCellStyle styleFontBold = wb.createCellStyle();
                HSSFFont font = wb.createFont();
                font.setBold(true);
                styleFontBold.setFont(font);

                HSSFSheet sheet = wb.createSheet("DataSheet");

                // Create header column
                HSSFRow row = sheet.createRow(0);
                int cellValueHeading = 0;
                for (int cno = 0; cno < tableView.getColumns().size(); cno++) {
                    if (columns != null && checkColumn(tableView, cno, columns)) {
                        blankColumns.add(cno);
                        continue;
                    }
                    HSSFCell cell = row.createCell(cellValueHeading);
                    cell.setCellStyle(styleFontBold);
                    cell.setCellValue(tableView.getColumns().get(cno).getText());
                    cellValueHeading++;
                }

                HSSFCell cell = null;
                int row_num = 0;
                for (int i = 0; i < tableView.getItems().size(); i++) {
                    row = sheet.createRow(i + 1);
                    int colIndex = 0;
                    for (int j = 0; j < tableView.getColumns().size(); j++) {
                        if (columns != null && checkColumn(tableView, j, columns))
                            continue;
                        cell = row.createCell(colIndex++);
                        if (tableView.getColumns().get(j).getId() != null
                                && tableView.getColumns().get(j).getId().toLowerCase().contains("date")
                                && tableView.getColumns().get(j).getCellData(i) != null
                                && !tableView.getColumns().get(j).getCellData(i).equals(MainApp.bundle.getString("na")))
                            cell.setCellValue(tableView.getColumns().get(j).getCellData(i) != null
                                    ? LocalDate.parse(tableView.getColumns().get(j).getCellData(i).toString()).format(
                                    MainApp.DATE_FORMATTER)
                                    : "");
                        else
                            cell.setCellValue(tableView.getColumns().get(j).getCellData(i) != null
                                    ? String.valueOf(tableView.getColumns().get(j).getCellData(i))
                                    : "");
                        sheet.autoSizeColumn(cellValueHeading);
                    }
                    row_num = i;
                }
                for (int i = 0; i < tableView2.getItems().size(); i++) {
                    row = sheet.createRow(i + 2 + row_num);
                    int colIndex = 0;
                    for (int j = 0; j < tableView2.getColumns().size(); j++) {
                        if (columns != null && checkColumn(tableView2, j, columns))
                            continue;
                        cell = row.createCell(colIndex++);

                        if (tableView2.getColumns().get(j).getId() != null
                                && tableView2.getColumns().get(j).getId().toLowerCase().contains("date")
                                && tableView2.getColumns().get(j).getCellData(i) != null
                                && !tableView2.getColumns().get(j).getCellData(i).equals(MainApp.bundle.getString("na")))
                            cell.setCellValue(tableView2.getColumns().get(j).getCellData(i) != null
                                    ? LocalDate.parse(tableView2.getColumns().get(j).getCellData(i).toString()).format(
                                    MainApp.DATE_FORMATTER)
                                    : "");
                        else
                            cell.setCellValue(tableView2.getColumns().get(j).getCellData(i) != null
                                    ? String.valueOf(tableView2.getColumns().get(j).getCellData(i))
                                    : "");


                    }
                }
                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                try {
                    wb.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return false;
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    wb.write(out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkColumn(TableView<?> tableView, int j, List<TableColItem> columns) {
        return columns.stream()
                .filter(p -> p.getColName().equals(tableView.getColumns().get(j).getId()) && !p.isVisible())
                .findAny().orElse(null) != null;
    }
}
