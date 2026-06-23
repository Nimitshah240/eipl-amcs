package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.service.ProductReceiptService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class ProductReceiptDeleteTask extends Task<Boolean> {
    private final String grnNo;

    public ProductReceiptDeleteTask(String grnNo) {
        this.grnNo = grnNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductReceiptService service = EmcsAppContext.getContext().getBean(ProductReceiptService.class);
            service.delete(grnNo, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
