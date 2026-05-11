package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "tbl_relationship")
public class Relationship {

    @Id
    private Integer relationshipCode;
    private String relationship;
    private String relationship_local;
    @Column(name = "is_active")
    private boolean active;

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.relationship, this.relationship_local);
    }

}