package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.service.UnitService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UnitLoadTask extends Task<List<Unit>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitLoadTask.class);

    @Override
    protected List<Unit> call() throws Exception {
        try {
            UnitService service = EmcsAppContext.getContext().getBean(UnitService.class);
            return service.findAll();
        } catch (Exception e) {
            LOGGER.error("Units fetch", e);
        }
        return null;
    }
}

