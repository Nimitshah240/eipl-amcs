package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_user_column")
public class RptUserColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private RptGridColumn gridColumn;

    @Column(name = "visible_flag")
    private Boolean visibleFlag;

    @Column(name = "width")
    private Integer width;

    @Column(name = "draft_width")
    private Integer draftWidth;

    @Column(name = "alignment", length = 20)
    private String alignment;

    @Column(name = "display_order")
    private Integer displayOrder;
}