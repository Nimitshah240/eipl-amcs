package com.eipl.amcs.master.insurance.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "insurance_master_audit")
public class InsuranceMasterAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer insuranceMasterCode;
    private LocalDate insuranceStartDate;
    private LocalDate insuranceEndDate;
    private LocalDate dcsEditStartDate;
    private LocalDate dcsEditEndDate;
    private Integer memberMinAge;
    private Integer memberMaxAge;
    private LocalDate insuranceFinalDate;
    private String insuranceDescription;
    private String unionCode;
    private Boolean isActive;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol4;
    private String xCol5;

    public String getxCol4() {
        return xCol4;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    public String getxCol5() {
        return xCol5;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

    @Override
    public String getTableName() {
        return "insurance_master_audit";
    }
}
