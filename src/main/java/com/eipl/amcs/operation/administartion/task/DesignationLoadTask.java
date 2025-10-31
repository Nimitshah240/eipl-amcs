package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.service.DesignationService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DesignationLoadTask extends Task<List<Designation>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesignationLoadTask.class);

    @Override
    protected List<Designation> call() throws Exception {
        try {
            DesignationService service = EmcsAppContext.getContext().getBean(DesignationService.class);
            List<Designation> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Designation fetch", e);
        }
        return null;
    }
}
