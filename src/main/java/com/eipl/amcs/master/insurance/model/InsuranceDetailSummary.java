package com.eipl.amcs.master.insurance.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "insurance_detail_summary")
public class InsuranceDetailSummary extends BaseModelTxn {
    @Id
    private Integer insuranceDetailSummaryCode;
    private Integer insuranceMasterCode;
    private String dcsCode;
    private String plantCode;
    private String unionCode;
    private String bmcCode;
    private String mccPlantCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;
    private String status;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol4;
    private String xCol5;
    private String dcsName;

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

    @Override
    public String getTableName() {
        return "tbl_insurance_detail_summary";
    }

    @Override
    public Object getId() {
        return this.getInsuranceDetailSummaryCode();
    }

    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        InsuranceDetailSummaryAudit audit = new InsuranceDetailSummaryAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setInsuranceDetailSummaryCode(this.getInsuranceDetailSummaryCode());
        audit.setInsuranceMasterCode(this.getInsuranceMasterCode());
        audit.setDcsCode(this.getDcsCode());
        audit.setDcsName(this.getDcsName());
        audit.setFromDate(this.getFromDate());
        audit.setToDate(this.getToDate());
        audit.setStatus(this.getStatus());
        audit.setOriginatingOrgCode(this.getOriginatingOrgCode());
        audit.setOriginatingOrgType(this.getOriginatingOrgType());
        audit.setOriginatingType(this.getOriginatingType());
        audit.setXCol4(this.getXCol4());
        audit.setXCol5(this.getXCol5());
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
