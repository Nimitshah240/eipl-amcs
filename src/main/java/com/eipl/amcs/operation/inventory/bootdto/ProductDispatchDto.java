package com.eipl.amcs.operation.inventory.bootdto;

import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDispatchDto {

    private ProductDispatch productReceipt;
    private List<ProductDispatchTransaction> receiptTxnTaxDtoList;
}
