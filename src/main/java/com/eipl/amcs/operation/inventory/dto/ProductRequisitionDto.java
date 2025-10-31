package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequisitionDto {
    private ProductRequisition productRequisition;
    private List<ProductRequisitionTransaction> transactionList;
}
