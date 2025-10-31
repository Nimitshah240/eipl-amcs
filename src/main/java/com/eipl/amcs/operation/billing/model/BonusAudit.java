package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.BonusSummaryDeserializer;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.BonusSummarySerialize;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bonus_audit")
public class BonusAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigDecimal milkQty;
    private BigDecimal milkAmount;
    private BigDecimal bonusAmount;

    private short status; //0-PENDING,1-DISBURSED
    private short type; //0-Union,1-Society
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BonusSummarySerialize.class)
    @JsonDeserialize(using = BonusSummaryDeserializer.class)
    @JoinColumn(name = "bonus_summary_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private BonusSummary bonusSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Union union;

    @Override
    public String getTableName() {
        return "bonus_audit";
    }

}
