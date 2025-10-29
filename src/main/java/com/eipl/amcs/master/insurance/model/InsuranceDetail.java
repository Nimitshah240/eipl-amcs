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
@Table(name = "insurance_detail")
public class InsuranceDetail extends BaseModelTxn {

    protected String xCol1;
    @Id
    private String insuranceDetailCode;
    private Integer insuranceMasterCode;
    private String srNo;
    private String unionCode;
    private String plantCode;
    private String bmcCode;
    private String mccPlantCode;
    private String dcsCode;
    private String dcsName;
    private String memberId;
    private String memberCode;
    private String memberName;
    private String adharNo;
    private String dob;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfJoiningScheme;
    private Integer age;
    private String genderCode;
    private String nomineeAdharNo;
    private String nomineeMemberName;
    private String status;
    private Boolean isDelete;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol4;
    private String xCol5;
    private String sysUpdatedBy;

    @Override
    public String getTableName() {
        return "tbl_insurance_detail";
    }


    @Override
    public Object getId() {
        return this.getInsuranceDetailCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        InsuranceDetailAudit audit = new InsuranceDetailAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setUnionCode(this.getUnionCode());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        audit.setInsuranceDetailCode(this.getInsuranceDetailCode());
        audit.setInsuranceMasterCode(this.getInsuranceMasterCode());
        audit.setSrNo(this.getSrNo());
        audit.setPlantCode(this.getPlantCode());
        audit.setBmcCode(this.getBmcCode());
        audit.setMccPlantCode(this.getMccPlantCode());
        audit.setDcsCode(this.getDcsCode());
        audit.setDcsName(this.getDcsName());
        audit.setMemberId(this.getMemberId());
        audit.setMemberCode(this.getMemberCode());
        audit.setMemberName(this.getMemberName());
        audit.setAdharNo(this.getAdharNo());
        audit.setDob(this.getDob());
        audit.setDateOfJoiningScheme(this.getDateOfJoiningScheme());
        audit.setAge(this.getAge());
        audit.setGenderCode(this.getGenderCode());
        audit.setNomineeAdharNo(this.getNomineeAdharNo());
        audit.setNomineeMemberName(this.getNomineeMemberName());
        audit.setStatus(this.getStatus());
        audit.setIsDelete(this.getIsDelete());
        audit.setOriginatingOrgCode(this.getOriginatingOrgCode());
        audit.setOriginatingOrgType(this.getOriginatingOrgType());
        audit.setOriginatingType(this.getOriginatingType());
        audit.setXCol4(this.getXCol4());
        audit.setXCol5(this.getXCol5());
        audit.setSysUpdatedBy(this.getSysUpdatedBy());
        return audit;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    @Override
    public String getxCol1() {
        return xCol1;
    }

    @Override
    public void setxCol1(String xCol1) {
        this.xCol1 = xCol1;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}