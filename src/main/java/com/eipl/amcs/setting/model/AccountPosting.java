package com.eipl.amcs.setting.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "account_posting")
@Getter
@Setter
@NoArgsConstructor
public class AccountPosting extends BaseModelTxn {
    @Id
    private String code;
    private LocalDate fromDate;
    private LocalDate toDate;
    //    private Integer fromShift;
//    private Integer toShift;
    private Integer postingType; // 1 - Consolidate, 2 - Day Wise, 3 - Payment Cycle wise
    private Short status; // 1-Draft, 2-Posted
    private Integer eventType; // DATA FROM EVENT MASTER TABLE
    private String dcsCode;
    private String bmcCode;
    private String mccPlantCode;
    private String plantCode;
    private String unionCode;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol4;
    private String xCol5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "from_shift", foreignKey = @ForeignKey(name = "fk_account_posting_from_shift_code"))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "to_shift", foreignKey = @ForeignKey(name = "fk_account_posting_to_shift_code"))
    private Shift toShift;

    public AccountPosting(String code, LocalDate fromDate, LocalDate toDate, Shift fromShift, Shift toShift,
                          Integer postingType, Integer eventType) {
        this.code = code;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromShift = fromShift;
        this.toShift = toShift;
        this.postingType = postingType;
        this.eventType = eventType;

        Society society = MainApp.identityDto.getSociety();
        this.dcsCode = MainApp.identityDto.getIdentity().getSocietyRefCode();
        this.bmcCode = society.getBmc().getCode();
        this.mccPlantCode = society.getMcc().getCode();
        this.plantCode = society.getPlant().getCode();
        this.unionCode = society.getUnion().getCode();

    }

    @Override
    public String getTableName() {
        return "tbl_account_posting";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }
}