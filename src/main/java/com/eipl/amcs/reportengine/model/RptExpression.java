package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_expression")
public class RptExpression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expression_id")
    private Long expressionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private RptComponent component;

    @Column(name = "expression_name", length = 100)
    private String expressionName;

    @Lob
    @Column(name = "expression_text")
    private String expressionText;

    @Column(name = "output_field", length = 100)
    private String outputField;
}