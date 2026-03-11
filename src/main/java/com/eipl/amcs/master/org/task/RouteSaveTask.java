package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.repository.RouteRepository;
import com.eipl.amcs.master.org.service.RouteService;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class RouteSaveTask extends Task<Object> {

    private final List<Route> routes;

    public RouteSaveTask(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    protected List<Route> call() throws Exception {
        RouteRepository routeRepository = EmcsAppContext.getContext().getBean(RouteRepository.class);
        if (routes == null)
            return null;
        return routeRepository.saveAll(routes);
    }
}
