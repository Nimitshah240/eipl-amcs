package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.DockDeserializer;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.serialize.DockSerialize;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_milk_collection_rejected")
public class RejectedMilkCollection extends BaseModelTxn {

    @Id
    private String milkCollectionRejectedCode;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime date;

    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal qty;
    private String remark;
    private String xCol4;
    private String xCol5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_rejected_milk_shift_code"))
    private Shift shift;

    @Override
    public String toString() {
        return "RejectedMilkCollection{" +
                "milkCollectionRejectedCode='" + milkCollectionRejectedCode + '\'' +
                ", date=" + date +
                ", fat=" + fat +
                ", snf=" + snf +
                ", qty=" + qty +
                ", remark='" + remark + '\'' +
                ", xCol4='" + xCol4 + '\'' +
                ", xCol5='" + xCol5 + '\'' +
                ", shift=" + shift +
                ", member=" + member +
                ", milkType=" + milkType +
                ", dock=" + dock +
                '}';
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_rejected_milk_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_rejected_milk_milk_type_code"))
    private MilkType milkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DockSerialize.class)
    @JsonDeserialize(using = DockDeserializer.class)
    @JoinColumn(name = "dock_code", foreignKey = @ForeignKey(name = "fk_rejected_milk_dock_code"))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;


    @Override
    public String getTableName() {
        return "tbl_milk_collection_rejected";
    }

    @Override
    public Object getId() {
        return this.milkCollectionRejectedCode;
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        return null;
    }

}