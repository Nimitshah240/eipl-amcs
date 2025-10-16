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
        service.save(route);
        return route;


//        RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//        String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ROUTE;
//        ResponseEntity<Route> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(route), Route.class);
//        if (response == null || response.getStatusCode() != HttpStatus.OK)
//            return response.getBody();
//        return null;
    }
}
