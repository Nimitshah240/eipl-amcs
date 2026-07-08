package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_lookup_parameter")
public class RptLookupParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookup_id")
    private RptLookup lookup;

    @Column(name = "parameter_name", length = 100)
    private String parameterName;

    @Column(name = "source_column", length = 100)
    private String sourceColumn;
}