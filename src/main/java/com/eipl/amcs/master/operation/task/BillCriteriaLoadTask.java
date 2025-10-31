package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BillCriteriaLoadTask extends Task<List<BillCriteria>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillCriteriaLoadTask.class);

    @Override
    protected List<BillCriteria> call() throws Exception {
        try {
            BillCriteriaService service = EmcsAppContext.getContext().getBean(BillCriteriaService.class);
            List<BillCriteria> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("BillCriteria fetch", e);
        }
        return null;
    }
}

