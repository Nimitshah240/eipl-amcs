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
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bmc_recording")
public class BmcRecording extends BaseModelTxn {

    @Id
    private Long code;
    @Column(name = "is_active")
    private Boolean isActive;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime recordingDateTime;
    private BigDecimal temperature;
    private BigDecimal weight;
    private String chillerNo;
    private String societyCode;
    private String unionCode;
    private String xCol4;
    private String xCol5;

    @Override
    public String getTableName() {
        return "bmc_recording";
    }

}
