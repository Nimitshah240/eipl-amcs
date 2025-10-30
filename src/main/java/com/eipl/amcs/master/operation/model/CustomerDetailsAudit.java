package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer_details_audit")
public class CustomerDetailsAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String aadharCardNo;
    private String accountNo;
    private String address;
    private String cstNo;
    private String email;
    private String pincode;
    private String ifsc;
    private String tinNo;
    private String panNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = CustomerSerialize.class)
    @JsonDeserialize(using = CustomerDeserializer.class)
    @JoinColumn(name = "customer_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Hamlet hamlet;

    @Override
    public String getTableName() {
        return "customer_details_audit";
    }

}
