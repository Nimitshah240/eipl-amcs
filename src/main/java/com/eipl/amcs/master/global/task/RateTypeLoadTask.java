package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.service.RateTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RateTypeLoadTask extends Task<List<RateType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateTypeLoadTask.class);

    @Override
    protected List<RateType> call() throws Exception {
        try {
            RateTypeService service = EmcsAppContext.getContext().getBean(RateTypeService.class);
            List<RateType> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("RateType fetch", e);
        }
        return null;
    }
}

