package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.service.ProductReceiptTransactionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductReceiptTransactionsByGrnNoLoadTask extends Task<List<ReceiptTxnTaxDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask.class);

    private final String code;

    public ProductReceiptTransactionsByGrnNoLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<ReceiptTxnTaxDto> call() throws Exception {
        try {
            ProductReceiptTransactionService service = EmcsAppContext.getContext().getBean(ProductReceiptTransactionService.class);
            List<ReceiptTxnTaxDto> list = service.findByProductReceipt(code);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductReceiptTransactions fetch", e);
        }
        return null;
    }
}
