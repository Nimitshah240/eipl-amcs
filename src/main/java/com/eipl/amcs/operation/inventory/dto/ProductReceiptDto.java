package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductReceiptDto {
    private ProductReceipt productReceipt;
    private List<ReceiptTxnTaxDto> receiptTxnTaxDtoList;
}
