package com.eipl.amcs.reportengine.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_group")
public class RptGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private RptComponent component;

    @Column(name = "group_level")
    private Integer groupLevel;

    @Column(name = "group_field", length = 100)
    private String groupField;

    @Column(name = "show_header")
    private Boolean showHeader = true;

    @Column(name = "show_footer")
    private Boolean showFooter = true;

    @Column(name = "start_new_page")
    private Boolean startNewPage = false;

    @Column(name = "collapsed")
    private Boolean collapsed = false;
}