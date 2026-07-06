package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_report_parameter")
public class RptReportParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_parameter_id")
    private Long reportParameterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private RptReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_id", nullable = false)
    private RptParameterMaster parameterMaster;

    @Column(name = "row_no")
    private Integer rowNo = 1;

    @Column(name = "column_no")
    private Integer columnNo = 1;

    @Column(name = "width")
    private Integer width = 180;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "required_flag")
    private Boolean requiredFlag = false;

    @Column(name = "visible_flag")
    private Boolean visibleFlag = true;
}