package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_style")
public class RptStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_id")
    private Long styleId;

    @Column(name = "style_name", length = 100)
    private String styleName;

    @Column(name = "report_heading_font", length = 100)
    private String reportHeadingFont;

    @Column(name = "report_heading_size")
    private Integer reportHeadingSize;

    @Column(name = "title_font", length = 100)
    private String titleFont;

    @Column(name = "title_size")
    private Integer titleSize;

    @Column(name = "subtitle_font", length = 100)
    private String subtitleFont;

    @Column(name = "subtitle_size")
    private Integer subtitleSize;

    @Column(name = "header_font", length = 100)
    private String headerFont;

    @Column(name = "header_size")
    private Integer headerSize;

    @Column(name = "data_font", length = 100)
    private String dataFont;

    @Column(name = "data_size")
    private Integer dataSize;

    @Column(name = "total_font", length = 100)
    private String totalFont;

    @Column(name = "total_size")
    private Integer totalSize;

    @Column(name = "bold_heading")
    private Boolean boldHeading;

    @Column(name = "bold_header")
    private Boolean boldHeader;

    @Column(name = "bold_total")
    private Boolean boldTotal;

    @Column(name = "show_grid")
    private Boolean showGrid;

    @Column(name = "active")
    private Boolean active = true;
}