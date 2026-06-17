package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.service.RejectedMilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RejectedMilkCollectionDeleteTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedMilkCollectionDeleteTask.class);
    private final String code;

    public RejectedMilkCollectionDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            RejectedMilkCollectionService service = EmcsAppContext.getContext().getBean(RejectedMilkCollectionService.class);
            Optional<RejectedMilkCollection> rejectedMilkCollection = service.findById(code);
            if (rejectedMilkCollection == null || !rejectedMilkCollection.isPresent())
                return false;

            service.delete(rejectedMilkCollection.get());
            return true;

        } catch (Exception e) {
            LOGGER.error("RejectedMilkCollection delete", e);
        }
        return false;
    }
}
