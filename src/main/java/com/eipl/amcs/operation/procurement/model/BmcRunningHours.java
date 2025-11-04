package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bmc_running_hours")
public class BmcRunningHours extends BaseModelTxn {

    @Id
    private Long code;
    @Column(name = "is_active")
    private Boolean isActive;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private int runningHoursDg;
    private int runningHoursPower;
    private int totalRunningHours;
    private String chillerNo;
    private String societyCode;
    private String unionCode;
    private String xCol4;
    private String xCol5;

    @Override
    public String getTableName() {
        return "bmc_running_hours";
    }

}
