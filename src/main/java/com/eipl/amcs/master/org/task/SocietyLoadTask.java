package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.service.SocietyService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SocietyLoadTask extends Task<List<Society>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyLoadTask.class);

    @Override
    protected List<Society> call() throws Exception {
        try {
            SocietyService service = EmcsAppContext.getContext().getBean(SocietyService.class);
            List<Society> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Society fetch", e);
        }
        return null;
    }
}
