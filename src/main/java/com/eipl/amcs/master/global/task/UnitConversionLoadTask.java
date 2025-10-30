package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.UnitConversion;
import com.eipl.amcs.master.global.service.UnitConversionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UnitConversionLoadTask extends Task<List<UnitConversion>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitConversionLoadTask.class);

    @Override
    protected List<UnitConversion> call() throws Exception {
        try {
            UnitConversionService service = EmcsAppContext.getContext().getBean(UnitConversionService.class);
            List<UnitConversion> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("UnitConversions fetch", e);
        }
        return null;
    }
}

