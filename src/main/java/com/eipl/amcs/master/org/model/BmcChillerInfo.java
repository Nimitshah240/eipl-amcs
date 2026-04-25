package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_bmc_chiller_info")
@Getter
@Setter
public class BmcChillerInfo extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chiller_info_code")
    private Integer chillerInfoCode;
    private String ownerName;
    private String rateType;
    private Integer chillingCapacity;
    private BigDecimal minQty;
    private String panNo;
    private BigDecimal tdsPercentage;
    private LocalDate installationDate;
    private String agreementNo;
    private LocalDate agreementFromDate;
    private LocalDate agreementToDate;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String chillerName;
    private BigDecimal fixRent;
    private String billingMethod;
    private String sapVendorCode;

    @Transient // TODO need to check in prompt - NImit
    private Integer agreementPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "dcs_code")
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne
    @JoinColumn(name = "mcc_plant_code", referencedColumnName = "code")
    private Mcc mcc;
    @ManyToOne
    @JoinColumn(name = "plant_code", referencedColumnName = "code")
    private Plant plant;
    @ManyToOne
    @JoinColumn(name = "union_code", referencedColumnName = "code")
    private Union union;
}