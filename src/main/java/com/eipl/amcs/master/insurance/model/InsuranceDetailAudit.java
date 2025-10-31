package com.eipl.amcs.master.insurance.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
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
@Table(name = "insurance_detail_audit")
public class InsuranceDetailAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    private LocalDate dateOfJoiningScheme;
    private String sysUpdatedBy;

    @Override
    public String getTableName() {
        return "insurance_detail_audit";
    }
}
