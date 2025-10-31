package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MilkDispatchByChallaNoLoadTask extends Task<MilkDispatch> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkDispatchByChallaNoLoadTask.class);
    private final String challanNo;

    public MilkDispatchByChallaNoLoadTask(String challanNo) {
        this.challanNo = challanNo;
    }

    @Override
    protected MilkDispatch call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            Optional<MilkDispatch> milkDispatch = service.findById(challanNo);
            if (milkDispatch == null || milkDispatch.isEmpty())
                return null;
            return milkDispatch.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
