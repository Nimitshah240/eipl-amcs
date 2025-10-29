package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.deserialize.BillHeadDeserializer;
import com.eipl.amcs.deserialize.FormulaDeserializer;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.deserialize.UnionDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.serialize.BillHeadSerialize;
import com.eipl.amcs.serialize.FormulaSerialize;
import com.eipl.amcs.serialize.SocietySerialize;
import com.eipl.amcs.serialize.UnionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * This class acts as a model for managing BillCriteria Audit.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bill_criteria_audit")
public class BillCriteriaAudit extends BaseModelAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 12)
    private String code;
    @Size(max = 100)
    private String criteria;
    @Size(max = 255)
    private String formula;
    @Column
    private LocalDate startDate;
    @Column
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BillHeadSerialize.class)
    @JsonDeserialize(using = BillHeadDeserializer.class)
    @JoinColumn(name = "bill_head_code", referencedColumnName = "code",
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union", "society", "hibernateLazyInitializer", "handler"})
    private BillHead billHeadCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = FormulaSerialize.class)
    @JsonDeserialize(using = FormulaDeserializer.class)
    @JoinColumn(name = "formula_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union", "society"})
    private Formula formulaCode;

    @Override
    public String getTableName() {
        return "bill_criteria_audit";
    }
}