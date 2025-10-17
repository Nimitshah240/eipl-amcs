package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class MilkDispatchPrevRecordGetTask extends Task<MilkDispatch> {
    private final LocalDateTime fromDate;

    public MilkDispatchPrevRecordGetTask(LocalDateTime fromDate) {
        this.fromDate = fromDate;

    }

    @Override
    protected MilkDispatch call() throws Exception {
        try {
            MilkDispatchRepository repository = EmcsAppContext.getContext().getBean(MilkDispatchRepository.class);
            return repository.findPreviousRecordOfGoodMilkType(fromDate).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
