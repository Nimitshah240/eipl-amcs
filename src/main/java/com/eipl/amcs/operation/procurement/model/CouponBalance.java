package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnitDeserializer;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coupon_balance")
public class CouponBalance extends BaseModelTxn {
    @Id
    @Column(name = "coupon_balance_code", length = 15)
    private String couponBalanceCode;

    private Double balance;
    private String consumerCode;
    private Integer consumerType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type", foreignKey = @ForeignKey(name = "fk_coupon_balance_milk_type"))
    @NotNull(message = "coupon.issue.validation.milktype.empty")
    private MilkType milkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnitDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_coupon_balance_union_code"))
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_coupon_balance_society_code"))
    private Society society;
    private String xCol4;
    private String xCol5;

    @Transient
    private String consumerName;
    @Transient
    private String consumerTypeString;


    public void setValuesInObject(String consumerCode, int consumerType, double balance, MilkType animalType) {
        this.setBalance(balance);
        this.setConsumerCode(consumerCode);
        this.setConsumerType(consumerType);
        this.setCreatedAt(LocalDateTime.now());
        this.setCreatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
        this.setSociety(MainApp.identityDto.getSociety() != null && MainApp.identityDto.getSociety().getCode() != null
                ? MainApp.identityDto.getSociety()
                : null);
        this.setMilkType(animalType);
        this.setUnion(MainApp.identityDto.getUnion() != null ? MainApp.identityDto.getUnion() : null);
    }

    @Override
    public Object getId() {
        return this.getCouponBalanceCode();
    }

    @Override
    public String getTableName() {
        return "coupon_balance";
    }


    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        CouponBalanceAudit audit = new CouponBalanceAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setCouponBalanceCode(this.getCouponBalanceCode());
        audit.setBalance(this.getBalance());
        audit.setConsumerCode(this.getConsumerCode());
        audit.setConsumerType(this.getConsumerType());
        audit.setMilkType(this.getMilkType());
        audit.setUnion(this.getUnion());
        audit.setSociety(this.getSociety());
        audit.setXCol4(this.getXCol4());
        audit.setXCol5(this.getXCol5());

        return audit;
    }
}
