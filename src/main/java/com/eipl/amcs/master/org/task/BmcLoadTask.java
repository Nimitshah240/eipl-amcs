package com.eipl.amcs.master.org.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.service.BmcService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class BmcLoadTask extends Task<List<Bmc>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmcLoadTask.class);

    @Override
    protected List<Bmc> call() throws Exception {
        try {
            BmcService service = EmcsAppContext.getContext().getBean(BmcService.class);
            List<Bmc> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Bmc fetch", e);
        }
        return null;
    }
}
