package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.master.account.service.StaffSalaryHeadService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StaffSalaryHeadLoadTask extends Task<List<StaffSalaryHead>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryHeadLoadTask.class);

    @Override
    protected List<StaffSalaryHead> call() throws Exception {
        try {
            StaffSalaryHeadService service = EmcsAppContext.getContext().getBean(StaffSalaryHeadService.class);
            List<StaffSalaryHead> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;

            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_SALARY_HEAD;
////            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
////                    .queryParam("society", MainApp.identityDto.getSociety().getCode());
//
//            ResponseEntity<StaffSalaryHead[]> response = restTemplate.getForEntity(url, StaffSalaryHead[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("StaffSalaryHead fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("StaffSalaryHead fetch", e);
        }
        return null;
    }
}
