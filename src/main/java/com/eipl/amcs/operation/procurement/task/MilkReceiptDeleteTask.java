package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class MilkReceiptDeleteTask extends Task<Boolean> {

    private final MilkReceipt receipt;


    public MilkReceiptDeleteTask(MilkReceipt receipt) {
        this.receipt = receipt;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MilkReceiptService service = EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            service.delete(receipt.getCode(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
