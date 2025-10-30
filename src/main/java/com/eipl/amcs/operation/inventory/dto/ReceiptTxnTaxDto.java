package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReceiptTxnTaxDto {
    private ProductReceiptTransaction transaction;
    private List<ProductReceiptTax> receiptTaxList;
}
