package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.BmcRunningHoursService;
import javafx.concurrent.Task;

public class BmcRunningHrsDeleteTask extends Task<Boolean> {
    private final Long code;

    public BmcRunningHrsDeleteTask(Long code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {

            BmcRunningHoursService service = EmcsAppContext.getContext().getBean(BmcRunningHoursService.class);
            service.deleteBmcRunningHours(code);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BMC_RUNNING_HRS + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//
//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
