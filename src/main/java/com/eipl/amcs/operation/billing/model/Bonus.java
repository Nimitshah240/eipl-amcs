package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.BonusSummaryDeserializer;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.BonusSummarySerialize;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name = "bonus")
public class Bonus extends BaseModelTxn {
    @Id
    private String code;
    private BigDecimal milkQty;
    private BigDecimal milkAmount;
    private BigDecimal bonusAmount;

    private short status; //0-PENDING,1-DISBURSED
    private short type; //0-Union,1-Society
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BonusSummarySerialize.class)
    @JsonDeserialize(using = BonusSummaryDeserializer.class)
    @JoinColumn(name = "bonus_summary_code", foreignKey = @ForeignKey(name = "fk_bonus_bonus_summary_code"))
    @JsonIgnoreProperties(value = {"union", "society"})
    private BonusSummary bonusSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_bonus_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_bonus_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "route", "bmc", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_bonus_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @Transient
    private BooleanProperty selected;

    @Column
    private String memberName;
    @Transient
    private Bank bank;
    @Column
    private String bankName;
    @Column
    private String accountNo;
    @Column
    private String ifsc;
    @Column
    private BigDecimal debantureAmount;
    @Column
    private BigDecimal kapaatAmount;
    @Column
    private BigDecimal totalAmount;

    public Bonus() {
        selected = new SimpleBooleanProperty();
    }

    @Override
    public String getTableName() {
        return "bonus";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        BonusAudit audit = new BonusAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setMilkAmount(this.getMilkAmount());
        audit.setMilkQty(this.getMilkQty());
        audit.setMember(this.getMember());
        audit.setBonusAmount(this.getBonusAmount());
        audit.setBonusSummary(this.getBonusSummary());
        audit.setUnion(this.getUnion());
        audit.setStatus(this.getStatus());
        audit.setType(this.getType());
        audit.setSociety(this.getSociety());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }

    public final boolean isSelected() {
        return this.selectedProperty().get();
    }

}
