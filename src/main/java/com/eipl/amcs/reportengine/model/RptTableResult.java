package com.eipl.amcs.reportengine.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Table(name = "rpt_table_result")
@Entity
@AllArgsConstructor
public class RptTableResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reportCode;
    private String respFieldName;
    private String respDispName;
    private Integer respSeq;
    private Integer dispSeq;
    @Column(name = "is_visible")
    private Boolean visible;
    private Integer width;
    private Integer reportOrientation; // 1-Portrait, 2-Landscape
    private Integer cellAlignment; // 1-Left, 2-Right, 3-Center

    @Transient
    private transient BooleanProperty selected;

    // 2. Safe Default Constructor for Hibernate
    public RptTableResult() {
        // Initialize as false by default to protect against early initialization null pointers
        this.selected = new SimpleBooleanProperty(false);
    }

    // 3. PostLoad hook updates the property after Hibernate maps database records
    @PostLoad
    private void initJavaFXProperty() {
        if (this.selected == null) {
            this.selected = new SimpleBooleanProperty(this.visible != null ? this.visible : false);
        } else {
            this.selected.set(this.visible != null ? this.visible : false);
        }
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
