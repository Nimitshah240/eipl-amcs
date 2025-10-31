package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MilkDispatchLoadTask extends Task<List<MilkDispatch>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkDispatchLoadTask.class);

    @Override
    protected List<MilkDispatch> call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            List<MilkDispatch> list = service.findAll();

            if (list == null || list.isEmpty()) return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MilkDispatches fetch", e);
        }
        return null;
    }
}