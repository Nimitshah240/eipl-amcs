package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.service.ProductSaleInstallmentService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductSaleInstallmentByOnlyInvoiceNoLoadTask extends Task<List<ProductSaleInstallment>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleInstallmentByOnlyInvoiceNoLoadTask.class);

    private final String code;

    public ProductSaleInstallmentByOnlyInvoiceNoLoadTask(String code) {
        this.code = code;

    }

    @Override
    protected List<ProductSaleInstallment> call() throws Exception {
        try {
            ProductSaleInstallmentService service = EmcsAppContext.getContext().getBean(ProductSaleInstallmentService.class);
            List<ProductSaleInstallment> list = service.fetchByPaymentCycle(code);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSaleToMemberInstallment fetch", e);
        }
        return null;
    }
}
