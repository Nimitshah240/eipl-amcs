package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_grid_column")
public class RptGridColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_id")
    private Long columnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private RptComponent component;

    @Column(name = "field_name", length = 100)
    private String fieldName;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "visible_flag")
    private Boolean visibleFlag = true;

    @Column(name = "alignment", length = 20)
    private String alignment;

    @Column(name = "width")
    private Integer width;

    @Column(name = "draft_width")
    private Integer draftWidth;

    @Column(name = "data_type", length = 30)
    private String dataType;

    @Column(name = "format_pattern", length = 100)
    private String formatPattern;

    @Column(name = "frozen_flag")
    private Boolean frozenFlag = false;

    @Column(name = "sortable_flag")
    private Boolean sortableFlag = true;

    @Column(name = "filterable_flag")
    private Boolean filterableFlag = true;

    @Column(name = "excel_export")
    private Boolean excelExport = true;

    @Column(name = "pdf_export")
    private Boolean pdfExport = true;
}