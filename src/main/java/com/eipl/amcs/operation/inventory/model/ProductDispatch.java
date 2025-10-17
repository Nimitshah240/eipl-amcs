package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_dispatch")
public class ProductDispatch extends BaseModel {
    @Id
    private String challanNo;
    private Boolean challanVerified;
    private LocalDateTime requisitionDate;
    private LocalDate dispatchDate;
    private String referenceNo;
    private String vehicleNo;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_dispatch_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_code", foreignKey = @ForeignKey(name = "fk_product_dispatch_route_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private Route route;

    @Override
    public String getTableName() {
        return "product_dispatch";
    }

    @Override
    public Object getId() {
        return this.getChallanNo();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductDispatchAudit audit = new ProductDispatchAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setChallanNo(this.getChallanNo());
        audit.setDispatchDate(this.getDispatchDate());
        audit.setRoute(this.getRoute());
        audit.setSociety(this.getSociety());
        audit.setChallanVerified(this.getChallanVerified());
        audit.setRequisitionDate(this.getRequisitionDate());
        audit.setUnionCode(this.getUnionCode());
        audit.setReferenceNo(this.getReferenceNo());
        audit.setVehicleNo(this.getVehicleNo());
        audit.setCreatedAt(this.getCreatedAt());

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
