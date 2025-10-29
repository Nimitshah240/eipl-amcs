package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.json.serialize.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer_details")
public class CustomerDetails extends BaseModelTxn {
    @Id
    @Size(max = 15)
    private String code;
    @Size(max = 255)
    private String aadharCardNo;
    @Size(max = 255)
    private String accountNo;
    @Size(max = 500)
    private String address;
    @Size(max = 255)
    private String cstNo;
    @Size(max = 255)
    private String email;
    @Size(max = 6)
    private String pincode;
    @Size(max = 255)
    private String ifsc;
    @Size(max = 255)
    private String tinNo;
    @Size(max = 255)
    private String panNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = CustomerSerialize.class)
    @JsonDeserialize(using = CustomerDeserializer.class)
    @JoinColumn(name = "customer_code", foreignKey = @ForeignKey(name = "fk_customer_detail_customer_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_customer_detail_bank_code"))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_customer_detail_branch_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village", "hamlet"})
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_customer_detail_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_customer_detail_state_code"))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_customer_detail_district_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_customer_detail_sub_district_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_customer_detail_village_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(name = "fk_customer_detail_hamlet_code"))
    @JsonIgnoreProperties(value = {"village"})
    private Hamlet hamlet;

    @Override
    public String getTableName() {
        return "customer_details";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        CustomerDetailsAudit audit = new CustomerDetailsAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setAadharCardNo(this.getAadharCardNo());
        audit.setAccountNo(this.getAccountNo());
        audit.setAddress(this.getAddress());
        audit.setCstNo(this.getCstNo());
        audit.setEmail(this.getEmail());
        audit.setPincode(this.getPincode());
        audit.setIfsc(this.getIfsc());
        audit.setTinNo(this.getTinNo());
        audit.setPanNo(this.getPanNo());
        audit.setCustomer(this.getCustomer());
        audit.setBank(this.getBank());
        audit.setBranch(this.getBranch());
        audit.setUnion(this.getUnion());
        audit.setState(this.getState());
        audit.setDistrict(this.getDistrict());
        audit.setSubDistrict(this.getSubDistrict());
        audit.setVillage(this.getVillage());
        audit.setHamlet(this.getHamlet());

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
