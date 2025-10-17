package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.BmcRunningHours;
import com.eipl.amcs.operation.procurement.service.BmcRunningHoursService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BmcRunningHrsSaveTask extends Task<Object> {
    private final BmcRunningHours dto;
    private final short update;

    public BmcRunningHrsSaveTask(BmcRunningHours dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            if (dto == null) {
                // Handle the case when dto is null
                return false;
            }

            BmcRunningHoursService service = EmcsAppContext.getContext().getBean(BmcRunningHoursService.class);

            if (this.update == 0) {
                service.save(dto);
            } else {
                service.update(dto);
            }

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url;
//            ResponseEntity<BmcRunningHrs> response;
//
//            if (this.update == 0) {
//                url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BMC_RUNNING_HRS;
//                // POST request for creating a new record
//                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), BmcRunningHrs.class);
//            } else {
//                url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BMC_RUNNING_HRS;
//                // PUT request for updating an existing record
//                Map<String, Object> uriVariables = new HashMap<>();
//                Long code = dto.getCode();
//                if (code == null) {
//                    // Handle the case when code is null
//                    return false;
//                }
//                uriVariables.put("code", code);
//                response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), BmcRunningHrs.class, uriVariables);
//            }
//
//            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
//                // Return true if the API call was successful
//                return true;
//            }
            return true;
        } catch (HttpStatusCodeException e) {
            // Handle exceptions from the API call and parse the error response
            String errorResponse = e.getResponseBodyAsString();
            EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return false if there was an issue with the API call
        return false;
    }
}