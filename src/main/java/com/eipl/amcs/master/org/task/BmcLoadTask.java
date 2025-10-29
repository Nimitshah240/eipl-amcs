package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.service.BmcService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BmcLoadTask extends Task<List<Bmc>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmcLoadTask.class);

    @Override
    protected List<Bmc> call() throws Exception {
        try {
            BmcService service = EmcsAppContext.getContext().getBean(BmcService.class);
            List<Bmc> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Bmc fetch", e);
        }
        return null;
    }
}
