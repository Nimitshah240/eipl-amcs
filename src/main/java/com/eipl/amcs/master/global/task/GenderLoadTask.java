package com.eipl.amcs.master.global.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.operation.service.MemberService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.eipl.amcs.MainApp.context;

public class GenderLoadTask extends Task<List<Gender>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenderLoadTask.class);

    @Override
    protected List<Gender> call() throws Exception {
        try {
            GenderService service = EmcsAppContext.getContext().getBean(GenderService.class);
            List<Gender> list = service.findAll();
            return list;
        } catch (Exception e) {
            LOGGER.error("Genders fetch", e);
        }
        return null;
    }
}

