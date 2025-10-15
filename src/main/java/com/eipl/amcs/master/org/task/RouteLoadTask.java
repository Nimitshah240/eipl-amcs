package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.service.RouteService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RouteLoadTask extends Task<List<Route>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteLoadTask.class);

    @Override
    protected List<Route> call() throws Exception {
        try {
            RouteService service = EmcsAppContext.getContext().getBean(RouteService.class);
            List<Route> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Route fetch", e);
        }
        return null;
    }
}
