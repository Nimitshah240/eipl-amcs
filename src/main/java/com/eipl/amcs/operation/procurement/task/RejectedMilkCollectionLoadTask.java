package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.service.RejectedMilkCollectionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RejectedMilkCollectionLoadTask extends Task<List<RejectedMilkCollection>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedMilkCollectionLoadTask.class);

    @Override
    protected List<RejectedMilkCollection> call() throws Exception {
        try {
            RejectedMilkCollectionService service = EmcsAppContext.getContext().getBean(RejectedMilkCollectionService.class);
            List<RejectedMilkCollection> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("RejectedMilkCollection fetch", e);
        }
        return null;
    }
}
