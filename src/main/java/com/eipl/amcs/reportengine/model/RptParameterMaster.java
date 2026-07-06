package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_parameter_master")
public class RptParameterMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    private Long parameterId;

    @Column(name = "parameter_code", unique = true, length = 50)
    private String parameterCode;

    @Column(name = "parameter_name", length = 100)
    private String parameterName;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "data_type", length = 30)
    private String dataType;

    @Column(name = "control_type", length = 30)
    private String controlType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookup_id")
    private RptLookup lookup;

    @Column(name = "default_value", length = 200)
    private String defaultValue;

    @Column(name = "validation_rule", length = 500)
    private String validationRule;

    @Column(name = "active")
    private Boolean active = true;
}