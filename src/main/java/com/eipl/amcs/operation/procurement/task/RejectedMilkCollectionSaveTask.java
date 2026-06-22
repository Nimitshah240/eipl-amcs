package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.service.RejectedMilkCollectionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RejectedMilkCollectionSaveTask extends Task<RejectedMilkCollection> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedMilkCollectionSaveTask.class);
    private final RejectedMilkCollection rejectedMilkCollection;
    private final short saveOrUpdate;

    public RejectedMilkCollectionSaveTask(RejectedMilkCollection rejectedMilkCollection, short saveOrUpdate) {
        this.rejectedMilkCollection = rejectedMilkCollection;
        this.saveOrUpdate = saveOrUpdate;
    }

    @Override
    protected RejectedMilkCollection call() throws Exception {
        try {
            RejectedMilkCollectionService service = EmcsAppContext.getContext().getBean(RejectedMilkCollectionService.class);
            if (saveOrUpdate == 0) {
                return service.save(rejectedMilkCollection);
            } else {
                return service.update(rejectedMilkCollection);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save/update rejected milk collection", e);
        }
        return null;
    }
}
