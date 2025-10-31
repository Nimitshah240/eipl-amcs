package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MilkReceiptLoadTask extends Task<List<MilkReceipt>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkReceiptLoadTask.class);

    @Override
    protected List<MilkReceipt> call() throws Exception {
        try {
            MilkReceiptService service = EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            List<MilkReceipt> list = service.findAll();

            if (list == null || list.isEmpty()) return null;
            return list;

        } catch (Exception e) {
            LOGGER.error("MilkDispatches fetch", e);
        }
        return null;
    }
}