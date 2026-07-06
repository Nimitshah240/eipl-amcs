package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_lookup")
public class RptLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lookup_id")
    private Long lookupId;

    @Column(name = "lookup_code", nullable = false, unique = true, length = 50)
    private String lookupCode;

    @Column(name = "lookup_name", length = 100)
    private String lookupName;

    @Column(name = "source_type", nullable = false, length = 30)
    private String sourceType; // MODEL, SQL, SP, STATIC, ENUM

    @Column(name = "source_value", length = 500)
    private String sourceValue;

    @Column(name = "value_field", length = 100)
    private String valueField;

    @Column(name = "display_field", length = 100)
    private String displayField;

    @Column(name = "filter_clause", length = 500)
    private String filterClause;

    @Column(name = "order_by", length = 200)
    private String orderBy;

    @Column(name = "active")
    private Boolean active = true;
}