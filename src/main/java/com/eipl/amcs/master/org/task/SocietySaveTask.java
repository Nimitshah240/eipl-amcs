package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.service.SocietyService;
import javafx.concurrent.Task;

public class SocietySaveTask extends Task<Object> {

    private final Society society;

    public SocietySaveTask(Society society) {
        this.society = society;
    }

    @Override
    protected Object call() throws Exception {
        SocietyService service = EmcsAppContext.getContext().getBean(SocietyService.class);
        if (society == null)
            return null;
        service.save(society);
        return society;


//        RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//        String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY;
//        ResponseEntity<Society> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(society), Society.class);
//        if (response == null || response.getStatusCode() != HttpStatus.OK)
//            return response.getBody();
//        return null;
    }
}
