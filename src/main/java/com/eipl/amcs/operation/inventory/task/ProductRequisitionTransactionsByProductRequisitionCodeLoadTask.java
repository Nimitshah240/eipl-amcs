package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionTransactionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductRequisitionTransactionsByProductRequisitionCodeLoadTask extends Task<ProductRequisitionDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRequisitionTransactionsByProductRequisitionCodeLoadTask.class);

    private final String code;

    public ProductRequisitionTransactionsByProductRequisitionCodeLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected ProductRequisitionDto call() throws Exception {
        try {
            ProductRequisitionTransactionService service = EmcsAppContext.getContext().getBean(ProductRequisitionTransactionService.class);
            ProductRequisitionDto list = service.findByProductReceipt(code);
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductReceiptTransactions fetch", e);
        }
        return null;
    }
}
