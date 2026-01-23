package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.MilkClassDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.MilkClassSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CouponBalance extends BaseModel {
    @Id
    @Column(name = "coupon_balance_code", length = 15)
    private String couponBalanceCode;

    private Double balance;
    private String consumerCode;
    private Integer consumerType;
    @JsonSerialize(using = MilkClassSerialize.class)
    @JsonDeserialize(using = MilkClassDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_class", foreignKey = @ForeignKey(name = "fk_coupon_credit_limit_milk_class1"))
    @NotNull(message = "coupon.issue.validation.grade.empty")
    private MilkClass milkClass;

    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_type", foreignKey = @ForeignKey(name = "fk_coupon_credit_limit_milk_type1"))
    @NotNull(message = "coupon.issue.validation.milktype.empty")
    private MilkType milkType;

    private String flgSentboxEntry;
    private String syncStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime syncTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_coupon_credit_limit_union_code1"))
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_coupon_credit_limit_society_code1"))
    private Society society;

//    @JsonSerialize(using = SubCenterSerializer.class)
//    @JsonDeserialize(using = SubCenterDeserializer.class)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sub_center_code", foreignKey = @ForeignKey(name = "fk_coupon_issue_sub_center_code"))
//    private SubCenter subCenterCode;

    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;

    private String xCol4;
    private String xCol5;


    public void setValuesInObject(String consumerCode, int consumerType, double balance, MilkType animalType,
                                  MilkClass milkClass) {
        this.setBalance(balance);
        this.setConsumerCode(consumerCode);
        this.setConsumerType(consumerType);
        this.setCreatedAt(LocalDateTime.now());
        this.setCreatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
        this.setSociety(MainApp.identityDto.getSociety() != null && MainApp.identityDto.getSociety().getCode() != null
                ? MainApp.identityDto.getSociety()
                : null);
        this.setMilkClass(milkClass);
        this.setMilkType(animalType);
        this.setUnion(MainApp.identityDto.getUnion() != null ? MainApp.identityDto.getUnion() : null);
    }
}
