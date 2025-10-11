package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.service.MilkClassService;
import com.eipl.amcs.master.global.service.ShiftService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.eipl.amcs.MainApp.context;

public class ShiftLoadTask extends Task<List<Shift>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiftLoadTask.class);

    @Override
    protected List<Shift> call() throws Exception {
        try {
            ShiftService service = EmcsAppContext.getContext().getBean(ShiftService.class);
            List<Shift> list = service.findAll();
            return list;
        } catch (Exception e) {
            LOGGER.error("Shifts fetch", e);
        }
        return null;
    }
}

