package com.eipl.amcs.operation.procurement.dto;

import javax.print.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.awt.*;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PrinterHelper implements Printable {
    private final DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    private final PrinterJob pj;
    private final Book book;
    private final Font font;
    private PrintJobWatcher pjw;
    private PrintService service;
    private List<String> lines = new ArrayList<>();


    public PrinterHelper(String printerName, String font, int fontSize) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        this.font = new Font(font, Font.PLAIN, fontSize);
        for (PrintService service1 : services) {
            if (service1.getName().equalsIgnoreCase(printerName)) {
                service = service1;
                break;
            }
        }
        pj = PrinterJob.getPrinterJob();
        book = new Book();
    }

    public void print(List<String> lines) {
        if (service != null) {
            this.lines = lines;
            book.append(this, getPageFormat(pj));
            pj.setPageable(book);
            try {
                pj.setPrintService(service);
                pj.print();
            } catch (PrinterException ex) {
                System.out.println("Problem In Print " + ex);
            }
        } else {
            System.out.println("Printer Is not available");
        }
    }

    public void forwardByLine(int line) {
        if (line > 0) {
            String str = "";
            for (int i = 0; i < line; i++) {
                str = str + (char) 27 + "J" + (char) 53;
            }
            InputStream ff1 = new ByteArrayInputStream(str.getBytes());
            Doc doc = new SimpleDoc(ff1, flavor, null);
            DocPrintJob jobff1 = service.createPrintJob();
            pjw = new PrintJobWatcher(jobff1);
            try {
                jobff1.print(doc, null);
            } catch (PrintException ex) {
//                Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            pjw.waitForDone();
        }
    }

    public void reverseByLine(int line) {
        if (line > 0) {
            String res = "";
            for (int i = 0; i < line; i++) {
                res = res + (char) 27 + "j" + (char) 53;
            }
            InputStream ff = new ByteArrayInputStream(res.getBytes());
            Doc docff = new SimpleDoc(ff, flavor, null);
            DocPrintJob jobff = service.createPrintJob();
            pjw = new PrintJobWatcher(jobff);
            try {
                jobff.print(docff, null);
            } catch (PrintException ex) {
                System.out.println("Forward Not Working !!");
            }
            pjw.waitForDone();
        }
    }

    private PageFormat getPageFormat(PrinterJob pj) {
        System.out.println("Page Format method");
        PageFormat pf = pj.defaultPage();
        int height = lines.size() * (font.getSize() + 4) + 10;
        if (lines.size() < 10) {
            height = height < 80 ? 80 : height + (height * 4 / 100);
        } else {
            height = height < 80 ? 80 : height - (height * 3 / 100);
        }
        System.out.println(height);
        Paper paper = pf.getPaper();
        paper.setSize(250, height);
        paper.setImageableArea(0, 0, 250, height);
        if (System.getProperty("os.name").toLowerCase().contains("win"))
            pf.setOrientation(PageFormat.PORTRAIT);
        else
            pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        pf.setPaper(paper);
        return pf;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        System.out.println("Print method");
        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setFont(font);
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            int y = 10;
            for (String line : lines) {
                if (line != null) {
                    g2d.drawString(line, 50, y);
                    y += font.getSize() + 2;
                }
            }
            result = PAGE_EXISTS;
        }
        return result;
    }

}

class PrintJobWatcher {

    boolean done = false;

    PrintJobWatcher(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() {
            @Override
            public void printJobCanceled(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobCompleted(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobFailed(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobNoMoreEvents(PrintJobEvent pje) {
                allDone();
            }

            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    System.out.println("Printing done ...");
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }

    public synchronized void waitForDone() {
        while (!done) {
            try {
                wait();
            } catch (InterruptedException ex) {
//                Logger.getLogger(PrintJobWatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
