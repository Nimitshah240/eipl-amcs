package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class MilkSummaryDataEntryTask extends Task<List<MilkCollection>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkSummaryDataEntryTask.class);
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    public MilkSummaryDataEntryTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<MilkCollection> collectionResultList = service.findAllBetween(fromDate, toDate);

            if (collectionResultList == null || collectionResultList.isEmpty()) return null;
            return collectionResultList;
        } catch (Exception e) {
            LOGGER.error("MilkSummaryDataEntry fetch", e);
        }
        return null;
    }
}
