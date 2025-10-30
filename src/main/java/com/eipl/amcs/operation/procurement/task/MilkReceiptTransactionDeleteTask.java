package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class MilkReceiptTransactionDeleteTask extends Task<Boolean> {

    private final String code;

    public MilkReceiptTransactionDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MilkReceiptService service = EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            Optional<MilkReceiptTransaction> receiptData = service.findTransactionById(code);

            if (receiptData == null || !receiptData.isPresent())
                return null;
            service.deleteTransaction(receiptData.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
