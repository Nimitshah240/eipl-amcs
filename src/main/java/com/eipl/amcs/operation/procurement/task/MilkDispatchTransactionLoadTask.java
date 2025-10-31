package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MilkDispatchTransactionLoadTask extends Task<List<MilkDispatchTransaction>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkDispatchTransactionLoadTask.class);

    private final String challanNo;

    public MilkDispatchTransactionLoadTask(String challanNo) {
        this.challanNo = challanNo;
    }

    @Override
    protected List<MilkDispatchTransaction> call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            List<MilkDispatchTransaction> milkDispatchTransactions = service.findDetailByChallanNo(challanNo);
            if (milkDispatchTransactions == null || milkDispatchTransactions.isEmpty()) return null;
            return milkDispatchTransactions;
        } catch (Exception e) {
            LOGGER.error("Milk dispatch transaction fetch", e);
        }
        return null;
    }
}
