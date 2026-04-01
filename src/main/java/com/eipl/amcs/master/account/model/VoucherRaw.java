package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.VoucherTypeDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.VoucherTypeSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "voucher_raw")
public class VoucherRaw extends BaseModelTxn {

    @Id

    private String code;
    private Boolean autoPosted;
    private Boolean cancelled;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;
    private String billNo;
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_voucher_society_code"))
    @JsonIgnoreProperties(value = {"hamlet", "village", "subDistrict", "district", "state", "route", "bmc", "mcc", "plant", "union", "branch", "bank"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherTypeSerialize.class)
    @JsonDeserialize(using = VoucherTypeDeserializer.class)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(name = "fk_voucher_voucher_type_code"))
    private VoucherType voucherType;

    private String unionCode;
    private String dockCode;
    private String financialYearsCode;

    private String processName;
    private String processReference;

    @Column(name = "x_col4")
    private String xCol4;
    @Column(name = "x_col5")
    private String xCol5;

    private String mccPlantCode;
    private String plantCode;
    private String bmcCode;

    @OneToMany(mappedBy = "voucherRaw", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<VoucherTransactionRaw> voucherTransactionRawList = new ArrayList<>();

    @OneToMany(mappedBy = "voucherRaw", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<VoucherSubLedgerRaw> voucherSubLedgerRawList = new ArrayList<>();
}

