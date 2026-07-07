package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_report")
public class RptReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_code")
    private Long reportCode;

    @Column(name = "report_name", nullable = false, length = 200)
    private String reportName;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasource_code")
    private RptDatasource datasource;

    @Column(name = "version_no")
    private Integer versionNo = 1;

    @Column(name = "is_active")
    private Boolean isActive = true;
}