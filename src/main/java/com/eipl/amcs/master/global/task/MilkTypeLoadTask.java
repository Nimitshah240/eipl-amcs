package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.MilkTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MilkTypeLoadTask extends Task<List<MilkType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkTypeLoadTask.class);

    @Override
    protected List<MilkType> call() throws Exception {
        try {
            MilkTypeService service = EmcsAppContext.getContext().getBean(MilkTypeService.class);
            List<MilkType> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MilkTypes fetch", e);
        }
        return null;
    }
}

