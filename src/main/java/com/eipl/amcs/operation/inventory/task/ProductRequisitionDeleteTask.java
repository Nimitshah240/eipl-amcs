package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class ProductRequisitionDeleteTask extends Task<Boolean> {

    private final String grnNo;

    public ProductRequisitionDeleteTask(String grnNo) {
        this.grnNo = grnNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductRequisitionService service = EmcsAppContext.getContext().getBean(ProductRequisitionService.class);
            service.delete(grnNo, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
