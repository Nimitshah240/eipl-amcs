package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.service.MilkClassService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MilkClassLoadTask extends Task<List<MilkClass>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkClassLoadTask.class);

    @Override
    protected List<MilkClass> call() throws Exception {
        try {
            MilkClassService service = EmcsAppContext.getContext().getBean(MilkClassService.class);
            List<MilkClass> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MilkClasss fetch", e);
        }
        return null;
    }
}

