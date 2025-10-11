package com.eipl.amcs.master.org.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.service.PlantService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class PlantLoadTask extends Task<List<Plant>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantLoadTask.class);

    @Override
    protected List<Plant> call() throws Exception {
        try {
            PlantService service = EmcsAppContext.getContext().getBean(PlantService.class);
            List<Plant> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Plant fetch", e);
        }
        return null;
    }
}
