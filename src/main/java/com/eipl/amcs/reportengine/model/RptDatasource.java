package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_datasource")
public class RptDatasource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datasource_id")
    private Long datasourceId;

    @Column(name = "datasource_name", length = 200)
    private String datasourceName;

    @Column(name = "datasource_type", length = 30)
    private String datasourceType;

    @Lob
    @Column(name = "definition")
    private String definition;

    @Column(name = "connection_name", length = 100)
    private String connectionName;

    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds = 60;

    @Column(name = "cache_enabled")
    private Boolean cacheEnabled = false;

    @Column(name = "before_execute", length = 200)
    private String beforeExecute;

    @Column(name = "after_execute", length = 200)
    private String afterExecute;

    @Column(name = "active")
    private Boolean active = true;
}