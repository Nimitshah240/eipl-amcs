package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_section")
public class RptSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private RptReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_section_id")
    private RptSection parentSection;

    @Column(name = "datasource_id")
    private Long datasourceId;

    @Column(name = "section_name", length = 100)
    private String sectionName;

    @Column(name = "section_type", length = 50)
    private String sectionType;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "visible_flag")
    private Boolean visibleFlag = true;

    @Column(name = "height")
    private Integer height;
}