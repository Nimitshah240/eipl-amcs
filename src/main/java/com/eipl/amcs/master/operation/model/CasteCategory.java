package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_caste_category")
public class CasteCategory {

    @Id
    private Integer casteCategoryCode;

    private String casteCategoryName;
    private String localName;

    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean isActive;
    private LocalDateTime updatedAt;
    private String updatedBy;


    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.casteCategoryName, this.localName);
    }
}