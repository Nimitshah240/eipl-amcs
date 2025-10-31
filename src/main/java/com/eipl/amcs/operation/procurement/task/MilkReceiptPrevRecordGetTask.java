package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.repository.MilkReceiptRepository;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class MilkReceiptPrevRecordGetTask extends Task<MilkReceipt> {
    private final LocalDateTime fromDate;

    public MilkReceiptPrevRecordGetTask(LocalDateTime fromDate) {
        this.fromDate = fromDate;

    }

    @Override
    protected MilkReceipt call() throws Exception {
        try {
            MilkReceiptRepository repository = EmcsAppContext.getContext().getBean(MilkReceiptRepository.class);
            return repository.findPreviousRecordOfGoodMilkType(fromDate).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
