package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.service.RouteService;
import javafx.concurrent.Task;

public class RouteSaveTask extends Task<Object> {

    private final Route route;

    public RouteSaveTask(Route route) {
        this.route = route;
    }

    @Override
    protected Object call() throws Exception {
        RouteService service = EmcsAppContext.getContext().getBean(RouteService.class);
        if (route == null)
            return null;
        return service.save(route);
    }
}
