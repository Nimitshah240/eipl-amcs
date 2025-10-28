package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_requisition_audit")
public class ProductRequisitionAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private LocalDateTime requisitionDate;
    private String description;
    private int entryType;
    private Boolean isCancel;
    private String status;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union", "mcc", "plant", "bmc", "state", "district", "subDistrict", "village", "hamlet", "bank", "branch", "route"})
    private Society society;
}
