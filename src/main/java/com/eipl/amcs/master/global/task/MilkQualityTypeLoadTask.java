package com.eipl.amcs.master.global.task;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.eipl.amcs.MainApp.context;

public class MilkQualityTypeLoadTask extends Task<List<MilkQualityType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkQualityTypeLoadTask.class);
    private MilkQualityTypeService service;

    @Override
    protected List<MilkQualityType> call() throws Exception {
        try {
            service = context.getBean(MilkQualityTypeService.class);
            List<MilkQualityType> list = service.findAll();
            return list;
        } catch (Exception e) {
            LOGGER.error("MilkQualityTypes fetch", e);
        }
        return null;
    }
}

