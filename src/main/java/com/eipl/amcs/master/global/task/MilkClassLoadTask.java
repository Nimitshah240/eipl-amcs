package com.eipl.amcs.master.global.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.service.MilkClassService;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.eipl.amcs.MainApp.context;

public class MilkClassLoadTask extends Task<List<MilkClass>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkClassLoadTask.class);

    @Override
    protected List<MilkClass> call() throws Exception {
        try {
            MilkClassService service = EmcsAppContext.getContext().getBean(MilkClassService.class);
            List<MilkClass> list = service.findAll();
            return list;
        } catch (Exception e) {
            LOGGER.error("MilkClasss fetch", e);
        }
        return null;
    }
}

