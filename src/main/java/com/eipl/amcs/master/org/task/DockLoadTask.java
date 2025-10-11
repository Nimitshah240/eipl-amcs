package com.eipl.amcs.master.org.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.service.DockService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class DockLoadTask extends Task<List<DockMilkTypeDto>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockLoadTask.class);

    @Override
    protected List<DockMilkTypeDto> call() throws Exception {
        try {
            DockService service = EmcsAppContext.getContext().getBean(DockService.class);
            List<DockMilkTypeDto> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Dock fetch", e);
        }
        return null;
    }
}
