package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.service.RejectedMilkCollectionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RejectedMilkCollectionLoadByFilterTask extends Task<List<RejectedMilkCollection>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedMilkCollectionLoadByFilterTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final Shift fromShift;
    private final Shift toShift;
    private final Dock dock;

    public RejectedMilkCollectionLoadByFilterTask(LocalDate fromDate, LocalDate toDate, Shift fromShift, Shift toShift, Dock dock) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromShift = fromShift;
        this.toShift = toShift;
        this.dock = dock;
    }

    @Override
    protected List<RejectedMilkCollection> call() throws Exception {
        try {
            RejectedMilkCollectionService service = EmcsAppContext.getContext().getBean(RejectedMilkCollectionService.class);
            LocalDateTime fromDt = LocalDateTime.of((fromDate), LocalTime.MIN);
            LocalDateTime toDt = LocalDateTime.of((toDate), LocalTime.MAX);

            // Assuming your service has a method with this signature
//            List<RejectedMilkCollection> list = service.findAllByFilter(fromDt, toDt, fromShift, toShift, dock);

//            if (list == null || list.isEmpty())
                return null;
//            return list;
        } catch (Exception e) {
            LOGGER.error("RejectedMilkCollection fetch by filter", e);
        }
        return null;
    }
}
