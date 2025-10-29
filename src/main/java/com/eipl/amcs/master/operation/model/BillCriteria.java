package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.BillHeadDeserializer;
import com.eipl.amcs.json.deserialize.FormulaDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.json.serialize.BillHeadSerialize;
import com.eipl.amcs.json.serialize.FormulaSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bill_criteria")
public class BillCriteria extends BaseModel {

    @Id
    @Size(max = 12)
    private String code;
    @Size(max = 100)
    private String criteria;
    @Size(max = 255)
    private String formula;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BillHeadSerialize.class)
    @JsonDeserialize(using = BillHeadDeserializer.class)
    @JoinColumn(name = "bill_head_code", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_bill_criteria_bill_head_code"))
    @JsonIgnoreProperties(value = {"union", "society", "hibernateLazyInitializer", "handler"})
    private BillHead billHeadCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_bill_criteria_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet", "hibernateLazyInitializer", "handler"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_bill_criteria_union_code"))
    @JsonIgnoreProperties(value = {"society", "bank", "branch", "state", "district", "subDistrict", "village", "hamlet", "hibernateLazyInitializer", "handler"})
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = FormulaSerialize.class)
    @JsonDeserialize(using = FormulaDeserializer.class)
    @JoinColumn(name = "formula_code", foreignKey = @ForeignKey(name = "fk_bill_criteria_formula_code"))
    @JsonIgnoreProperties(value = {"union", "society", "hibernateLazyInitializer", "handler"})
    private Formula formulaCode;

    @Override
    public String getTableName() {
        return "bill_criteria";
    }

    /**
     * Method gives the code of the bill head upon calling this method (specially - customUpdate)
     *
     * @return Object
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public Object getId() {
        return this.getCode();
    }

    /**
     * Method creates the bill_head_audit upon deleting or updating bill head.
     *
     * @param operation
     * @param user
     * @return JsonAndTableBuilder
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        try {
            BillCriteriaAudit audit = new BillCriteriaAudit();
            audit.setOperationType(operation);
            audit.setAuditCreatedBy(user);

            audit.setCode(this.getCode());
            audit.setCriteria(this.getCriteria());
            audit.setFormula(this.getFormula());
            audit.setFormulaCode(this.getFormulaCode());
            audit.setStartDate(this.getStartDate());
            audit.setEndDate(this.getEndDate());
            audit.setBillHeadCode(this.getBillHeadCode());
            audit.setUnion(this.getUnion());
            audit.setSociety(this.getSociety());

            audit.setCreatedAt(this.getCreatedAt());
            audit.setCreatedBy(this.getCreatedBy());
            audit.setUpdatedAt(this.getUpdatedAt());
            audit.setUpdatedBy(this.getUpdatedBy());
            audit.setActive(this.isActive());
            audit.setXCol1(this.getXCol1());
            audit.setXCol2(this.getXCol2());
            audit.setXCol3(this.getXCol3());

            return audit;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}