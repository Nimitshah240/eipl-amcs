package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.service.GenderService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GenderLoadTask extends Task<List<Gender>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenderLoadTask.class);

    @Override
    protected List<Gender> call() throws Exception {
        try {
            GenderService service = EmcsAppContext.getContext().getBean(GenderService.class);
            List<Gender> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Genders fetch", e);
        }
        return null;
    }
}

