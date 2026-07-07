package com.eipl.amcs.reportengine.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {
    public static CellStyle centerAlignment(HSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle border(HSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    public static CellStyle centerAlignmentWithBorder(HSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    public static CellStyle centerAlignmentWithBorderAndBoldFont(HSSFWorkbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle centerAlignmentWithBorderAndBoldFontBackground(HSSFWorkbook wb) {
        return null;
    }

    public static CellStyle getHeaderCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 20);
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle getSubHeaderCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 16);
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle getSubHeaderCellStyle2(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 12);
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle getTableHeaderCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 12);
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    public static CellStyle getTableTextCentreCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 12);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle getTableTextLeftCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 12);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle getPrevMaintenaceDoneCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 12);
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    public static CellStyle getPrevMaintenacePendingCellStyle(Workbook wb) {
        Font fontBold = wb.createFont();
        fontBold.setFontName("Book Antiqua");
        fontBold.setFontHeightInPoints((short) 12);
        fontBold.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);
        cellStyle.setWrapText(true);
        return cellStyle;
    }
}