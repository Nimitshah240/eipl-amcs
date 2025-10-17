package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MilkReceiptDto {

    private MilkReceipt milkReceipt;
    private List<MilkReceiptTransaction> milkReceiptTransaction;

//    public MilkReceiptDto(MilkReceipt milkReceipt, List<MilkReceiptTransaction> milkReceiptTransaction) {
//        this.milkReceipt = milkReceipt;
//        this.milkReceiptTransaction = milkReceiptTransaction;
//    }
//
//    public MilkReceiptDto(){
//
//    }
//    public MilkReceipt getMilkReceipt() {
//        return milkReceipt;
//    }
//
//    public void setMilkReceipt(MilkReceipt milkReceipt) {
//        this.milkReceipt = milkReceipt;
//    }
//
//    public List<MilkReceiptTransaction> getMilkReceiptTransaction() {
//        return milkReceiptTransaction;
//    }
//
//    public void setMilkReceiptTransaction(List<MilkReceiptTransaction> milkReceiptTransaction) {
//        this.milkReceiptTransaction = milkReceiptTransaction;
//    }
}
