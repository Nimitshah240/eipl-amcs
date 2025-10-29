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
@Table(name = "insurance_detail_summary_audit")
public class InsuranceDetailSummaryAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "insurance_detail_summary_code")
    private Integer insuranceDetailSummaryCode;

    @Column(name = "insurance_master_code")
    private Integer insuranceMasterCode;

    @Column(name = "dcs_code", length = 12)
    private String dcsCode;
    @Column(name = "dcs_name", length = 255)
    private String dcsName;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "originating_org_code", length = 15)
    private String originatingOrgCode;

    @Column(name = "originating_org_type", length = 15)
    private String originatingOrgType;

    @Column(name = "originating_type")
    private Integer originatingType;
    @Column(name = "x_col4", length = 255)
    private String xCol4;

    @Column(name = "x_col5", length = 255)
    private String xCol5;

    @Override
    public String getTableName() {
        return "insurance_detail_summary_audit";
    }
}
