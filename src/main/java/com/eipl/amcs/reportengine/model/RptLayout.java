package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_layout")
public class RptLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layout_id")
    private Long layoutId;

    @Column(name = "layout_code", unique = true, length = 50)
    private String layoutCode;

    @Column(name = "layout_name", length = 150)
    private String layoutName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active")
    private Boolean active = true;
}