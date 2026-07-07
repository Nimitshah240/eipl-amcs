package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_datasource")
public class RptDatasource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datasource_code")
    private Long datasourceCode;

    @Column(name = "datasource_name", nullable = false, length = 200)
    private String datasourceName;

    @Column(name = "datasource_type", nullable = false, length = 50)
    private String datasourceType;

    @Lob
    @Column(name = "definition")
    private String definition;

    @Column(name = "is_active")
    private Boolean isActive = true;
}