package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.MeetingAgendaDeserializer;
import com.eipl.amcs.json.deserialize.MomDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.MeetingAgendaSerialize;
import com.eipl.amcs.json.serialize.MomSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "mom_action")
public class MomAction extends BaseModelTxn {

    @Id
    private String code;
    private String action_taken;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private short meetingType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MomSerialize.class)
    @JsonDeserialize(using = MomDeserializer.class)
    @JoinColumn(name = "mom_code", foreignKey = @ForeignKey(name = "fk_mom_action_mom_code"))
    @JsonIgnoreProperties(value = {"society", "union", "meetingAgenda"})
    private Mom mom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MeetingAgendaSerialize.class)
    @JsonDeserialize(using = MeetingAgendaDeserializer.class)
    @JoinColumn(name = "meeting_agenda_code", foreignKey = @ForeignKey(name = "fk_mom_action_meeting_agenda_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private MeetingAgenda meetingAgenda;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_mom_action_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_mom_action_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @Override
    public String getTableName() {
        return "mom_action";
    }
}
