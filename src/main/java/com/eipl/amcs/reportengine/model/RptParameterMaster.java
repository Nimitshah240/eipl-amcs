package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_parameter_master")
public class RptParameterMaster {

    @Id
    @Column(name = "parameter_master_code", length = 255)
    private String parameterMasterCode;

    @Column(name = "parameter_master_name", nullable = false, length = 200)
    private String parameterMasterName;

    @Column(name = "data_type", nullable = false, length = 50)
    private String dataType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookup_code")
    private RptLookup lookup;

    @Column(name = "is_active")
    private Boolean isActive = true;
}