package com.eipl.amcs.reportengine.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "rpt_component")
public class RptComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id")
    private Long componentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private RptSection section;

    @Column(name = "component_type", length = 50)
    private String componentType;

    @Column(name = "binding_type", length = 30)
    private String bindingType;

    @Column(name = "binding_value", length = 500)
    private String bindingValue;

    @Column(name = "x_position")
    private Integer xPosition;

    @Column(name = "y_position")
    private Integer yPosition;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "style_id")
    private Long styleId;

    @Column(name = "visible_flag")
    private Boolean visibleFlag = true;
}