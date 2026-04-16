package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coupon_balance_audit")
public class CouponBalanceAudit extends BaseModelAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String couponBalanceCode;
    private Double balance;
    private String consumerCode;
    private Integer consumerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkType milkType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;
    private String plantCode;
    private String mccCode;
    private String bmcCode;
    private String xCol4;
    private String xCol5;
}
