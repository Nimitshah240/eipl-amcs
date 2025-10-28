package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_credit_limit")
public class MemberCreditLimit extends BaseModelTxn {

    @Id
    @Size(max = 15)
    private String code;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;
    private Short consumerType; //0-Member, 1-Nonmember ...
    @Size(max = 25)
    private String consumerCode;
    @Size(max = 3)
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_members_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "member_credit_limit";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberCreditLimitAudit audit = new MemberCreditLimitAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setCode(this.getCode());
        audit.setConsumerCode(this.getConsumerCode());
        audit.setConsumerType(this.getConsumerType());
        audit.setUnionCode(this.getUnionCode());
        audit.setBalance(this.getBalance());
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
}
