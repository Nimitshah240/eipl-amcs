package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import com.eipl.amcs.master.account.service.StaffSalaryMappingService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StaffSalaryMappingLoadTask extends Task<List<StaffSalaryMapping>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryMappingLoadTask.class);

    @Override
    protected List<StaffSalaryMapping> call() throws Exception {
        try {

            StaffSalaryMappingService service = EmcsAppContext.getContext().getBean(StaffSalaryMappingService.class);

            List<StaffSalaryMapping> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_SALARY_MAPPING;
////            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
////                    .queryParam("society", MainApp.identityDto.getSociety().getCode());
//
//            ResponseEntity<StaffSalaryMapping[]> response = restTemplate.getForEntity(url, StaffSalaryMapping[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("StaffSalaryMapping fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("StaffSalaryMapping fetch", e);
        }
        return null;
    }
}
