package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_credit_limit_transaction")
public class MemberCreditLimitTransaction extends BaseModelTxn {

    @Id
    private String code;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal newValue;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal oldValue;
    private String consumerCode;
    // 1-Member, 2-Non member, 3-Institute, 4-Vendor, 5-Consumner
    private int consumerType;
    private String referenceCode;
    private String transactionType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_members_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    private String unionCode;

    @Override
    public String getTableName() {
        return "member_credit_limit_transaction";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberCreditLimitTransactionAudit audit = new MemberCreditLimitTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setBalance(this.getBalance());
        audit.setNewValue(this.getNewValue());
        audit.setOldValue(this.getOldValue());
        audit.setConsumerCode(this.getConsumerCode());
        audit.setConsumerType(this.getConsumerType());
        audit.setReferenceCode(this.getReferenceCode());
        audit.setTransactionDate(this.getTransactionDate());
        audit.setTransactionType(this.getTransactionType());
        audit.setSociety(this.getSociety());
        audit.setUnionCode(this.getUnionCode());

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
