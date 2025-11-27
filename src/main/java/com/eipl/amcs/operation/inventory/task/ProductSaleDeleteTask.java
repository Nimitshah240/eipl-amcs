package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class ProductSaleDeleteTask extends Task<Boolean> {

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
            throw new RuntimeException(e);
        }
//        return null;
    }
}
