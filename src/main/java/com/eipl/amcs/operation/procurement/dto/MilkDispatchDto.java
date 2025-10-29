package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MilkDispatchDto {

    private MilkDispatch milkDispatch;
    private List<MilkDispatchTransaction> milkDispatchTransaction;

//    public MilkDispatchDto(MilkDispatch milkDispatch, List<MilkDispatchTransaction> milkDispatchTransaction) {
//        this.milkDispatch = milkDispatch;
//        this.milkDispatchTransaction = milkDispatchTransaction;
//    }
//
//    public MilkDispatchDto() {
//    }
//
//    public MilkDispatch getMilkDispatch() {
//        return milkDispatch;
//    }
//
//    public void setMilkDispatch(MilkDispatch milkDispatch) {
//        this.milkDispatch = milkDispatch;
//    }
//
//    public List<MilkDispatchTransaction> getMilkDispatchTransaction() {
//        return milkDispatchTransaction;
//    }
//
//    public void setMilkDispatchTransaction(List<MilkDispatchTransaction> milkDispatchTransaction) {
//        this.milkDispatchTransaction = milkDispatchTransaction;
//    }
}
