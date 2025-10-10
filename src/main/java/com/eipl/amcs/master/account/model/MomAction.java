package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "mom_action")
public class MomAction extends BaseModelTxn {

    @Id
    private String code;
    @Size(max = 500)
    private String action_taken;
    private LocalDate date;
    private short meetingType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mom_code", foreignKey = @ForeignKey(name = "fk_mom_action_mom_code"))
    @JsonIgnoreProperties(value = {"society", "union", "meetingAgenda"})
    private Mom mom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_agenda_code", foreignKey = @ForeignKey(name = "fk_mom_action_meeting_agenda_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private MeetingAgenda meetingAgenda;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_mom_action_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_mom_action_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @Override
    public String getTableName() {
        return "mom_action";
    }
}
