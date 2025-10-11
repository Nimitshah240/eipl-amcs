package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BillHeadLoadTask extends Task<List<BillHead>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillHeadLoadTask.class);
    private BillHeadService service;

    @Override
    protected List<BillHead> call() throws Exception {
        try {

            List<BillHead> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("BillHead fetch", e);
        }
        return null;
    }
}

