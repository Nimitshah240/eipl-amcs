package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductRequisitionDeleteTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductSaleRateByProductLoadTask.class);

    private final String grnNo;

    public ProductRequisitionDeleteTask(String grnNo) {
        this.grnNo = grnNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductRequisitionService service = EmcsAppContext.getContext().getBean(ProductRequisitionService.class);
            service.delete(grnNo, CommonUtil.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
