package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "scheme_rate")
public class SchemeRate extends BaseModelTxn {
    @Id
    private String schemeRateCode;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime fromDate;
    private Integer fromShift;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime toDate;
    private Integer toShift;
    private BigDecimal rtpl;
    private String rateClass;
    private String description;
    private String unionCode;
    private Boolean isMccWiseRate;
    private Boolean isMemberRate;
    private Boolean isActive;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol4;
    private String xCol5;

    @Override
    public String getTableName() {
        return "tbl_scheme_rate";
    }

    @Override
    public Object getId() {
        return this.getSchemeRateCode();
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }
}