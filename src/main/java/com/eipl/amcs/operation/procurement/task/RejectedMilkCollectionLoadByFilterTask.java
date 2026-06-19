package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.service.RejectedMilkCollectionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class RejectedMilkCollectionLoadByFilterTask extends Task<List<RejectedMilkCollection>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedMilkCollectionLoadByFilterTask.class);

    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    public RejectedMilkCollectionLoadByFilterTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<RejectedMilkCollection> call() throws Exception {
        try {
            RejectedMilkCollectionService service = EmcsAppContext.getContext().getBean(RejectedMilkCollectionService.class);
            List<RejectedMilkCollection> list = service.findAllByFilter(fromDate, toDate);

            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("RejectedMilkCollection fetch by filter", e);
        }
        return null;
    }
}
