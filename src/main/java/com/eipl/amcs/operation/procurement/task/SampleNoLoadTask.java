package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class SampleNoLoadTask extends Task<Number> {
    private final LocalDateTime date;
    private final Dock dock;
    private final MilkType milkType;

    public SampleNoLoadTask(LocalDateTime date, Dock dock, MilkType milkType) {
        this.date = date;
        this.dock = dock;
        this.milkType = milkType;
    }

    @Override
    protected Number call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            return service.fetchNextSampleNo(date, dock.getDockNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
