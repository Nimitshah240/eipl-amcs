package com.eipl.amcs.reportengine.util;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.utils.CommonUtils;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PdfExportUtil {

    public static <T> void exportTableToPdf(TableView<T> tableView, String filePath, List<RptTableResult> listRptTableResult) {
        listRptTableResult.removeIf(p -> p.getVisible() == false);
        listRptTableResult.sort(Comparator.comparing(RptTableResult::getDispSeq));

        List<TableColumn<T, ?>> activeColumns = new ArrayList<>();
        for (TableColumn<T, ?> col : tableView.getColumns()) {
            if (col.getText() != null && !col.getText().trim().isEmpty()) {
                activeColumns.add(col);
            }
        }

        int columnCount = activeColumns.size();
        if (columnCount == 0)
            return;

        Document document = new Document(listRptTableResult.get(0).getReportOrientation() == 1 ? PageSize.A4 : PageSize.A4.rotate(), 26, 26, 26, 26);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            PdfHeaderFooterHelper eventHelper = new PdfHeaderFooterHelper();
            writer.setPageEvent(eventHelper);
            document.open();

            PdfPTable pdfTable = new PdfPTable(columnCount);
            pdfTable.setWidthPercentage(100);

            float[] relativeWidths = new float[columnCount];
            for (int i = 0; i < columnCount; i++) {
                relativeWidths[i] = (float) activeColumns.get(i).getWidth();
            }
            pdfTable.setWidths(relativeWidths);
            pdfTable.setHeaderRows(1);

            Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font cellFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            for (TableColumn<T, ?> column : activeColumns) {
                PdfPCell cell = new PdfPCell(new Phrase(column.getText(), headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(2);
                pdfTable.addCell(cell);
            }
            pdfTable.setKeepTogether(true);

            for (T rowItem : tableView.getItems()) {
                for (int i = 0; i < activeColumns.size(); i++) {
                    TableColumn<T, ?> column = activeColumns.get(i);
                    Object cellValue = null;

                    if (column.getCellValueFactory() != null) {
                        cellValue = column.getCellData(rowItem);
                    }

                    String cellText = "";
                    if (cellValue instanceof Boolean) {
                        cellText = (Boolean) cellValue ? "Yes" : "No";
                    } else if (cellValue instanceof LocalDate) {
                        cellText = CommonUtils.getLocalString((LocalDate) cellValue);
                    } else if (cellValue != null) {
                        cellText = cellValue.toString();
                    }

                    PdfPCell cell = new PdfPCell(new Phrase(cellText, cellFont));
                    cell.setHorizontalAlignment(
                            getPdfCellAlignment(listRptTableResult.get(i).getCellAlignment())
                    );
                    cell.setPadding(2);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);
            document.close();
            System.out.println("PDF successfully generated at: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getPdfCellAlignment(Integer cellAlignment) {
        int val = Element.ALIGN_CENTER;
        switch (cellAlignment) {
            case 1:
                val = Element.ALIGN_LEFT;
                break;
            case 2:
                val = Element.ALIGN_RIGHT;
                break;
            default:
                val = Element.ALIGN_CENTER;
                break;
        }
        return val;
    }

    public static <T> void exportTableToPdf(TableView<T> tableView, String filePath) {


        List<TableColumn<T, ?>> activeColumns = new ArrayList<>();
        for (TableColumn<T, ?> col : tableView.getColumns()) {
            if (col.getText() != null && !col.getText().trim().isEmpty()) {
                activeColumns.add(col);
            }
        }

        int columnCount = activeColumns.size();
        if (columnCount == 0)
            return;

        Document document = new Document(PageSize.A4, 26, 26, 26, 26);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            PdfHeaderFooterHelper eventHelper = new PdfHeaderFooterHelper();
            writer.setPageEvent(eventHelper);
            document.open();

            PdfPTable pdfTable = new PdfPTable(columnCount);
            pdfTable.setWidthPercentage(100);

            float[] relativeWidths = new float[columnCount];
            for (int i = 0; i < columnCount; i++) {
                relativeWidths[i] = (float) activeColumns.get(i).getWidth();
            }
            pdfTable.setWidths(relativeWidths);
            pdfTable.setHeaderRows(1);
            Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font cellFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            if (MainApp.getLocale().equalsIgnoreCase("gu")) {
                BaseFont baseFont = BaseFont.createFont(
                        "C:/Windows/Fonts/Nirmala.ttf",
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED
                );

                headerFont = new Font(baseFont, 11, Font.BOLD);
                cellFont = new Font(baseFont, 10, Font.NORMAL);
            }


            for (TableColumn<T, ?> column : activeColumns) {
                PdfPCell cell = new PdfPCell(new Phrase(column.getText(), headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(2);
                pdfTable.addCell(cell);
            }
            pdfTable.setKeepTogether(true);

            for (T rowItem : tableView.getItems()) {
                for (int i = 0; i < activeColumns.size(); i++) {
                    TableColumn<T, ?> column = activeColumns.get(i);
                    Object cellValue = null;

                    if (column.getCellValueFactory() != null) {
                        cellValue = column.getCellData(rowItem);
                    }

                    String cellText = "";
                    if (cellValue instanceof Boolean) {
                        cellText = (Boolean) cellValue ? "Yes" : "No";
                    } else if (cellValue instanceof LocalDate) {
                        cellText = CommonUtils.getLocalString((LocalDate) cellValue);
                    } else if (cellValue != null) {
                        cellText = cellValue.toString();
                    }

                    PdfPCell cell = new PdfPCell(new Phrase(cellText, cellFont));
                    cell.setPadding(2);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);
            document.close();
            System.out.println("PDF successfully generated at: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
