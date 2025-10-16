package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import com.eipl.amcs.master.account.service.StaffSalaryMappingService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class StaffSalaryMappingSaveTask extends Task<Object> {
    private final List<StaffSalaryMapping> paymentCycleList;
    private final short update;

    public StaffSalaryMappingSaveTask(List<StaffSalaryMapping> paymentCycleList, short update) {
        this.paymentCycleList = paymentCycleList;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {

            StaffSalaryMappingService service = EmcsAppContext.getContext().getBean(StaffSalaryMappingService.class);

            if (this.update == 0) {
                return service.save(paymentCycleList, CommonUtil.setIdentityHeader());
            }

//
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_SALARY_MAPPING;

//            ResponseEntity<Object> response = null;
//            if (this.update == 0) {
//                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(paymentCycleList), Object.class);
//                if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                    return null;
//                return response.getBody().toString();
//            } else {
////                url = url + "/{code}";
////                Map<String, Object> uriVariables = new HashMap<>();
////                uriVariables.put("code", paymentCycleList.get(0).getCode());
////                response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(paymentCycleList.get(0)), Object.class);
////                if (response == null || response.getStatusCode() != HttpStatus.OK)
////                    return null;
////                return response.getBody();
//            }

        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
