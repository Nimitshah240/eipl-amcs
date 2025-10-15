package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.service.MccService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MccLoadTask extends Task<List<Mcc>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MccLoadTask.class);

    @Override
    protected List<Mcc> call() throws Exception {
        try {
            MccService service = EmcsAppContext.getContext().getBean(MccService.class);
            List<Mcc> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Mcc fetch", e);
        }
        return null;
    }
}
