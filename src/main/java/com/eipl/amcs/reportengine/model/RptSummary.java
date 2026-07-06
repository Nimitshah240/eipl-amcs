package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_summary")
public class RptSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Long summaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private RptComponent component;

    @Column(name = "field_name", length = 100)
    private String fieldName;

    @Column(name = "summary_function", length = 30)
    private String summaryFunction;

    @Column(name = "summary_position", length = 30)
    private String summaryPosition;

    @Column(name = "format_pattern", length = 50)
    private String formatPattern;
}