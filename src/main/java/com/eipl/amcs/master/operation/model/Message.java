package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.global.model.Shift;
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
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_message")
public class Message extends BaseModel {

    @Id
    @Column(name = "message_code")
    private String code;
    private String message;
    private String messageLocal;
    @Column(name = "flg_sentbox_entry")
    private String flgSentboxEntry;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDate fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDate toDate;
    @Column(name = "is_delete")
    private Boolean isDelete;
    @Column(name = "sync_status")
    private String syncStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sync_timestamp")
    private LocalDateTime syncTimestamp;
    @Column(name = "originating_org_code")
    private String originatingOrgCode;
    @Column(name = "originating_org_type")
    private String originatingOrgType;
    @Column(name = "originating_type")
    private Integer originatingType;
    @Column(name = "x_col4")
    private String xCol4;
    @Column(name = "x_col5")
    private String xCol5;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_message_society_code"))
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society societyCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_shift_code", foreignKey = @ForeignKey(name = "fk_message_for_shift_code"))
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    private Shift forShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_shift_code", foreignKey = @ForeignKey(name = "fk_message_from_shift_code"))
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_shift_code", foreignKey = @ForeignKey(name = "fk_message_to_shift_code"))
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    private Shift toShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_message_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @Override
    public String getTableName() {
        return "tbl_message";
    }
}
