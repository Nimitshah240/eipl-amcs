package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.task.ProductSaleRateByProductLoadTask;
import com.eipl.amcs.operation.inventory.service.ProductReceiptService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductReceiptDeleteTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleRateByProductLoadTask.class);

    private final String grnNo;

    public ProductReceiptDeleteTask(String grnNo) {
        this.grnNo = grnNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductReceiptService service = EmcsAppContext.getContext().getBean(ProductReceiptService.class);
            service.delete(grnNo, CommonUtil.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
