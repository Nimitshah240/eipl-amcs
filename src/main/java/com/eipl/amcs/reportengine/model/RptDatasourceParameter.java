package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_datasource_parameter")
public class RptDatasourceParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datasource_parameter_code")
    private Long datasourceParameterCode;

    @Column(name = "datasource_parameter_name", nullable = false, length = 200)
    private String datasourceParameterName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasource_code", nullable = false)
    private RptDatasource datasource;

    @Column(name = "parameter_order")
    private Integer parameterOrder;

    @Column(name = "default_value", length = 200)
    private String defaultValue;

    @Column(name = "required_flag")
    private Boolean requiredFlag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_master_code", nullable = false)
    private RptParameterMaster parameterMaster;

    @Column
    private String dataType;
}