package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class MilkDispatchTransactionDeleteTask extends Task<Boolean> {

    private final String code;

    public MilkDispatchTransactionDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);

            Optional<MilkDispatchTransaction> dispatchData = service.findTransactionById(code);
            if (dispatchData == null || !dispatchData.isPresent())
                return null;
            service.deleteTransaction(dispatchData.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
