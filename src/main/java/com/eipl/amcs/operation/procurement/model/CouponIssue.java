package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
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
import com.eipl.amcs.master.operation.model.CustomerDetailsAudit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coupon_issue")
public class CouponIssue extends BaseModelTxn {

    @Id
    private String code;
    private Double amount;
    private String consumerCode;
    private Integer consumerType;
    private String flgSentboxEntry;
    @Column(name = "is_active")
    private boolean active;
    private Boolean isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;
    private String syncStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime syncTimestamp;
    private String voucherNo;


//    @JsonSerialize(using = CollectionPointSerializer.class)
//    @JsonDeserialize(using = CollectionPointDeserializer.class)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "collection_point_no", foreignKey = @ForeignKey(name = "fk_coupon_issue_collection_point_no"))
//    private CollectionPoint collectionPointNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_coupon_issue_society_code"))
    private Society society;

    @JsonSerialize(using = MilkClassSerialize.class)
    @JsonDeserialize(using = MilkClassDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_class", foreignKey = @ForeignKey(name = "fk_coupon_issue_milk_class"))
    @NotNull(message = "coupon.issue.validation.grade.empty")
    private MilkClass milkClass;

    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_type", foreignKey = @ForeignKey(name = "fk_coupon_issue_milk_type"))
    @NotNull(message = "coupon.issue.validation.milktype.empty")
    private MilkType milkType;

//    @JsonSerialize(using = SubCenterSerializer.class)
//    @JsonDeserialize(using = SubCenterDeserializer.class)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sub_center_code", foreignKey = @ForeignKey(name = "fk_coupon_issue_sub_center_code"))
//    private SubCenter subCenterCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_coupon_issue_union_code"))
    private Union union;

    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;

    private String xCol4;
    private String xCol5;


    @Transient
    @JsonIgnore
    private String consumerTypeString;
    @Transient
    @JsonIgnore
    private String consumerName;

    public String getConsumerTypeString() {
        if (this.consumerType == null) return "";
        switch (this.consumerType) {
            case 1:
            case 2:
                return "member";
            case 3:
                return "vendor";
            case 4:
                return "institute";
            default:
                return "consumer";
        }
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public String getTableName() {
        return "coupon_issue";
    }


    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        CouponIssueAudit audit = new CouponIssueAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCouponIssueCode(this.getCode());
        audit.setAmount(this.getAmount());
        audit.setConsumerCode(this.getConsumerCode());
        audit.setConsumerType(this.getConsumerType());
        audit.setFlgSentboxEntry(this.getFlgSentboxEntry());
        audit.setIsDelete(this.isDelete);
        audit.setIssueDate(this.getIssueDate());
        audit.setSyncStatus(this.getSyncStatus());
        audit.setSyncTimestamp(this.getSyncTimestamp());
        audit.setVoucherNo(this.getVoucherNo());

        audit.setMilkClass(this.getMilkClass());
        audit.setMilkType(this.getMilkType());
        audit.setUnion(this.getUnion());
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
