package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "milk_receipt")
public class MilkReceipt extends BaseModelTxn {

    @Id
    private String code;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private LocalDate receiptDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challan_no", foreignKey = @ForeignKey(name = "fk_milk_receipt_challan_no"))
    @JsonIgnoreProperties(value = {"fromShift", "toShift", "society", "union"})
    private MilkDispatch milkDispatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_shift_code", foreignKey = @ForeignKey(name = "fk_milk_receipt_from_shift"))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_shift_code", foreignKey = @ForeignKey(name = "fk_milk_receipt_to_shift"))
    private Shift toShift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_milk_receipt_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_milk_receipt_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;


    @Override
    public String getTableName() {
        return "milk_receipt";
    }


//	@Override
//	public JsonAndTableBuilder getAuditModel(String operation, String user) {
//		MilkDispatchAudit audit = new MilkDispatchAudit();
//		audit.setOperationType(operation);
//		audit.setAuditCreatedBy(user);
//
//		audit.setChallanNo(this.getChallanNo());
////		audit.setDestinationCode(this.getDestinationCode());
////		audit.setDipStickReadingClosing(this.getDipStickReadingClosing());
////		audit.setDipStickReadingOpening(this.getDipStickReadingOpening());
////		audit.setDispatchType(this.getDispatchType());
////		audit.setDestinationType(this.getDestinationType());
////		audit.setHeadLoadKms(this.getHeadLoadKms());
////		audit.setRouteNo(this.getRouteNo());
//		audit.setFromDate(this.getFromDate());
//		audit.setToDate(this.getToDate());
//		audit.setFromShift(this.getFromShift());
//		audit.setToShift(this.getToShift());
////		audit.setVehicleInTime(this.getVehicleInTime());
////		audit.setVehicleOutTime(this.getVehicleOutTime());
////		audit.setVehicleNo(this.getVehicleNo());
//		audit.setSociety(this.getSociety());
//		audit.setUnion(this.getUnion());
////		audit.setBrokenSealNo(this.getBrokenSealNo());
////		audit.setNewSealNo(this.getNewSealNo());
//
//		audit.setCreatedAt(this.getCreatedAt());
//		audit.setCreatedBy(this.getCreatedBy());
//		audit.setUpdatedAt(this.getUpdatedAt());
//		audit.setUpdatedBy(this.getUpdatedBy());
//		audit.setXCol1(this.getXCol1());
//		audit.setXCol2(this.getXCol2());
//		audit.setXCol3(this.getXCol3());
//
//		return audit;
//	}

}