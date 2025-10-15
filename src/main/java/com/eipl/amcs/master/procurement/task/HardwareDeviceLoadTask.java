package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.service.HardwareDeviceService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HardwareDeviceLoadTask extends Task<List<HardwareDevice>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareDeviceLoadTask.class);

    @Override
    protected List<HardwareDevice> call() throws Exception {
        try {
            HardwareDeviceService service = EmcsAppContext.getContext().getBean(HardwareDeviceService.class);
            List<HardwareDevice> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("HardwareDevices fetch", e);
        }
        return null;
    }
}

