package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.service.StateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StateLoadTask extends Task<List<State>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateLoadTask.class);

    @Override
    protected List<com.eipl.amcs.master.geo.model.State> call() throws Exception {
        try {
            StateService service = EmcsAppContext.getContext().getBean(StateService.class);
            List<com.eipl.amcs.master.geo.model.State> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("States fetch", e);
        }
        return null;
    }
}

