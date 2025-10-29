package com.eipl.amcs.master.insurance.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "insurance_master")
public class InsuranceMaster extends BaseModel {
    @Id
    private Integer insuranceMasterCode;
    private LocalDate insuranceStartDate;
    private LocalDate insuranceEndDate;
    private LocalDate dcsEditStartDate;
    private LocalDate dcsEditEndDate;
    private Integer memberMinAge;
    private Integer memberMaxAge;
    private LocalDate insuranceFinalDate;
    private String insuranceDescription;
    private String status;
    private String unionCode;
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
        return "insurance_master";
    }

    @Override
    public Object getId() {
        return this.getInsuranceMasterCode();
    }


    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        InsuranceMasterAudit audit = new InsuranceMasterAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setInsuranceMasterCode(this.getInsuranceMasterCode());
        audit.setInsuranceStartDate(this.getInsuranceStartDate());
        audit.setInsuranceEndDate(this.getInsuranceEndDate());
        audit.setDcsEditStartDate(this.getDcsEditStartDate());
        audit.setDcsEditEndDate(this.getDcsEditEndDate());
        audit.setMemberMinAge(this.getMemberMinAge());
        audit.setMemberMaxAge(this.getMemberMaxAge());
        audit.setInsuranceFinalDate(this.getInsuranceFinalDate());
        audit.setInsuranceDescription(this.getInsuranceDescription());
        audit.setUnionCode(this.getUnionCode());
        audit.setOriginatingOrgCode(this.getOriginatingOrgCode());
        audit.setOriginatingOrgType(this.getOriginatingOrgType());
        audit.setOriginatingType(this.getOriginatingType());
        audit.setxCol4(this.getxCol4());
        audit.setxCol5(this.getxCol5());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        return audit;
    }
}
