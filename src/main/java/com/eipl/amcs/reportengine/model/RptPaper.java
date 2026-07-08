package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "rpt_paper")
public class RptPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paper_id")
    private Long paperId;

    @Column(name = "paper_name", length = 100)
    private String paperName;

    @Column(name = "gui_page_size", length = 30)
    private String guiPageSize;

    @Column(name = "orientation", length = 20)
    private String orientation;

    @Column(name = "margin_left", precision = 5, scale = 2)
    private BigDecimal marginLeft;

    @Column(name = "margin_right", precision = 5, scale = 2)
    private BigDecimal marginRight;

    @Column(name = "margin_top", precision = 5, scale = 2)
    private BigDecimal marginTop;

    @Column(name = "margin_bottom", precision = 5, scale = 2)
    private BigDecimal marginBottom;

    @Column(name = "draft_page_size", length = 30)
    private String draftPageSize;

    @Column(name = "draft_columns")
    private Integer draftColumns;

    @Column(name = "draft_lines")
    private Integer draftLines;

    @Column(name = "draft_margin_left")
    private Integer draftMarginLeft;

    @Column(name = "draft_margin_right")
    private Integer draftMarginRight;

    @Column(name = "draft_margin_top")
    private Integer draftMarginTop;

    @Column(name = "draft_margin_bottom")
    private Integer draftMarginBottom;

    @Column(name = "active")
    private Boolean active = true;
}