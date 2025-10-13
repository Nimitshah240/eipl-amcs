package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.service.ProductSaleTransactionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductSaleTransactionsByInvoiceNoLoadTask extends Task<List<SaleTxnTaxDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask.class);

    private final String code;

    public ProductSaleTransactionsByInvoiceNoLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<SaleTxnTaxDto> call() throws Exception {
        try {

            ProductSaleTransactionService service = EmcsAppContext.getContext().getBean(ProductSaleTransactionService.class);
            List<SaleTxnTaxDto> list = service.findByProductSale(code);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSaleTransactions fetch", e);
        }
        return null;
    }
}
