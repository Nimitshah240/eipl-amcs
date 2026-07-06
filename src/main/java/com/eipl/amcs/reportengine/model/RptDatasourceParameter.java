package com.eipl.amcs.reportengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "rpt_datasource_parameter")
public class RptDatasourceParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datasource_parameter_id")
    private Long datasourceParameterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasource_id", nullable = false)
    private RptDatasource datasource;

    @Column(name = "parameter_name", length = 100)
    private String parameterName;

    @Column(name = "parameter_order")
    private Integer parameterOrder;

    @Column(name = "parameter_code", length = 50)
    private String parameterCode;

    @Column(name = "default_value", length = 200)
    private String defaultValue;

    @Column(name = "required_flag")
    private Boolean requiredFlag = true; // Maps to tinyint(1) with your default 1 (true)
}