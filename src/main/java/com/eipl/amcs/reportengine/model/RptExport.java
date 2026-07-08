package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_export")
public class RptExport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "export_id")
    private Long exportId;

    @Column(name = "export_name", length = 100)
    private String exportName;

    @Column(name = "excel_enabled")
    private Boolean excelEnabled;

    @Column(name = "pdf_enabled")
    private Boolean pdfEnabled;

    @Column(name = "csv_enabled")
    private Boolean csvEnabled;

    @Column(name = "repeat_header")
    private Boolean repeatHeader;

    @Column(name = "include_parameters")
    private Boolean includeParameters;

    @Column(name = "include_footer")
    private Boolean includeFooter;

    @Column(name = "auto_fit_columns")
    private Boolean autoFitColumns;

    @Column(name = "show_page_number")
    private Boolean showPageNumber;

    @Column(name = "show_print_date")
    private Boolean showPrintDate;
}