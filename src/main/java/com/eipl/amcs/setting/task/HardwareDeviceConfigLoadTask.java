package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.service.HardwareDeviceConfigService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HardwareDeviceConfigLoadTask extends Task<List<HardwareDeviceConfig>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareDeviceConfigLoadTask.class);

    @Override
    protected List<HardwareDeviceConfig> call() throws Exception {
        try {
            HardwareDeviceConfigService service = EmcsAppContext.getContext().getBean(HardwareDeviceConfigService.class);
            List<HardwareDeviceConfig> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("HardwareDeviceConfigs fetch", e);
        }
        return null;
    }
}

