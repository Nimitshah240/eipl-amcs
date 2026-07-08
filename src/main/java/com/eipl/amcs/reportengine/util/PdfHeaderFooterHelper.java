package com.eipl.amcs.reportengine.util;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfHeaderFooterHelper extends PdfPageEventHelper {

    private PdfTemplate totalPagesPlaceholder;
    private BaseFont baseFont;
    private final Font footerFont;
    private final String printDateTime;

    public PdfHeaderFooterHelper() {
        this.footerFont = new Font(Font.HELVETICA, 9, Font.ITALIC);
        this.printDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        try {
            this.baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        totalPagesPlaceholder = cb.createTemplate(30, 16);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Rectangle page = document.getPageSize();

        float footerY = document.bottomMargin() - 15;
        float leftX = document.leftMargin();
        float rightX = page.getWidth() - document.rightMargin();

        cb.beginText();
        cb.setFontAndSize(baseFont, 9);
        cb.showTextAligned(Element.ALIGN_LEFT, "Print Date: " + printDateTime, leftX, footerY, 0);
        cb.endText();

        String pageText = "Page " + writer.getPageNumber() + " of ";
        float textWidth = baseFont.getWidthPoint(pageText, 9);
        
        cb.beginText();
        cb.setFontAndSize(baseFont, 9);
        cb.showTextAligned(Element.ALIGN_RIGHT, pageText, rightX - 15, footerY, 0);
        cb.endText();
        cb.addTemplate(totalPagesPlaceholder, rightX - 15, footerY);
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalPagesPlaceholder.beginText();
        totalPagesPlaceholder.setFontAndSize(baseFont, 9);
        totalPagesPlaceholder.showText(String.valueOf(writer.getPageNumber()));
        totalPagesPlaceholder.endText();
    }
}
