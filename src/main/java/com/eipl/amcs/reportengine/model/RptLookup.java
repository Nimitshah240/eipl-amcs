package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_lookup")
public class RptLookup {

    @Id
    @Column(name = "lookup_code", length = 255)
    private String lookupCode;

    @Column(name = "lookup_name", nullable = false, length = 200)
    private String lookupName;

    @Column(name = "source_type", length = 50)
    private String sourceType;

    @Lob
    @Column(name = "source_value")
    private String sourceValue;

    @Lob
    @Column(name = "filter_clause")
    private String filterClause;

    @Column(name = "order_by", length = 200)
    private String orderBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "value_field")
    private String valueField;

    @Column(name = "display_field")
    private String displayField;
}