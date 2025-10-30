package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MilkReceiptTransactionLoadTask extends Task<List<MilkReceiptTransaction>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkReceiptTransactionLoadTask.class);

    private final String challanNo;

    public MilkReceiptTransactionLoadTask(String challanNo) {
        this.challanNo = challanNo;
    }

    @Override
    protected List<MilkReceiptTransaction> call() throws Exception {
        try {
            MilkReceiptService service = EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            List<MilkReceiptTransaction> list = (service.findDetailByChallanNo(challanNo));
            if (list == null || list.isEmpty()) return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Milk dispatch transaction fetch", e);
        }
        return null;
    }
}
