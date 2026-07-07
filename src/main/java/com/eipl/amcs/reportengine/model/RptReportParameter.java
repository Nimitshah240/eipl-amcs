package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_report_parameter")
public class RptReportParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_parameter_code")
    private Long reportParameterCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_code", nullable = false)
    private RptReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_master_code", nullable = false)
    private RptParameterMaster parameterMaster;

    @Column(name = "row_no")
    private Integer rowNo;

    @Column(name = "column_no")
    private Integer columnNo;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "required_flag")
    private Boolean requiredFlag = false;

    @Column(name = "visible_flag")
    private Boolean visibleFlag = true;

    @Column(name = "default_value", length = 200)
    private String defaultValue;
}