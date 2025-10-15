package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.service.StateService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class StateLoadTask extends Task<List<State>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateLoadTask.class);

    @Override
    protected List<com.eipl.amcs.master.geo.model.State> call() throws Exception {
        try {
            StateService service= EmcsAppContext.getContext().getBean(StateService.class);
            List<com.eipl.amcs.master.geo.model.State> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STATE;
//            ResponseEntity<com.eipl.amcs.master.geo.model.State[]> response = restTemplate.getForEntity(url, com.eipl.amcs.master.geo.model.State[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("States fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("States fetch", e);
        }
        return null;
    }
}

