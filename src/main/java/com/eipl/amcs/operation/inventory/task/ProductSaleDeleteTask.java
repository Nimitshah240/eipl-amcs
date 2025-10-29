package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductSaleDeleteTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductSaleRateByProductLoadTask.class);

    private final String invoiceNo;

    public ProductSaleDeleteTask(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductSaleService service = EmcsAppContext.getContext().getBean(ProductSaleService.class);
            service.delete(invoiceNo, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
