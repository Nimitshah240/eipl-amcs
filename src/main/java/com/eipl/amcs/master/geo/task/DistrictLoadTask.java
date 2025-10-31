package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.service.DistrictService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DistrictLoadTask extends Task<List<District>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistrictLoadTask.class);
    private com.eipl.amcs.master.geo.model.State state;

    public DistrictLoadTask() {
    }

    public DistrictLoadTask(com.eipl.amcs.master.geo.model.State state) {
        this.state = state;
    }

    @Override
    protected List<District> call() throws Exception {
        try {
            DistrictService service = EmcsAppContext.getContext().getBean(DistrictService.class);
            List<District> list;
            if (state == null)
                list = service.findAll();
            else
                list = service.findAll(state.getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Districts fetch", e);
        }
        return null;
    }
}

