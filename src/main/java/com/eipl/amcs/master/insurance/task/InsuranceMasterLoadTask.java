package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InsuranceMasterLoadTask extends Task<List<InsuranceMaster>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceMasterLoadTask.class);

    @Override
    protected List<InsuranceMaster> call() throws Exception {
        try {
            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            List<InsuranceMaster> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Insurance fetch", e);
        }
        return null;
    }
}
