package com.eipl.amcs.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class BaseModelTxn implements Serializable, JsonAndTableBuilder {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;
    @Size(max = 15)
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;
    @Size(max = 15)
    private String updatedBy;

    @Column(name = "x_col1", length = 255)
    private String xCol1;
    @Column(name = "x_col2", length = 255)
    private String xCol2;
    @Column(name = "x_col3", length = 255)
    private String xCol3;

    public void setInitData() {
        createdAt = LocalDateTime.now();
        if (createdBy == null || createdBy.isEmpty())
            createdBy = "System";
    }

    public void setupdateData() {
        updatedAt = LocalDateTime.now();
        if (updatedBy == null || updatedBy.isEmpty() || updatedBy.equalsIgnoreCase("null"))
            updatedBy = "System";
    }

    @Override
    public String getUserInfo() {
        return this.getUpdatedBy();
    }

    public String getxCol1() {
        return xCol1;
    }

    public void setxCol1(String xCol1) {
        this.xCol1 = xCol1;
    }

    public String getXCol1() {
        return xCol1;
    }

    public String getxCol2() {
        return xCol2;
    }

    public void setxCol2(String xCol2) {
        this.xCol2 = xCol2;
    }

    public String getXCol2() {
        return xCol2;
    }

    public String getxCol3() {
        return xCol3;
    }

    public void setxCol3(String xCol3) {
        this.xCol3 = xCol3;
    }

    public String getXCol3() {
        return xCol3;
    }

}
