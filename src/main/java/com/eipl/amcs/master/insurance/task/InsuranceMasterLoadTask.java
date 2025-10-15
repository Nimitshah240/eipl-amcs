package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class InsuranceMasterLoadTask extends Task<List<InsuranceMaster>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceMasterLoadTask.class);

    @Override
    protected List<InsuranceMaster> call() throws Exception {
        try {
            InsuranceMasterService service= EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            List<InsuranceMaster> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("insurance", MainApp.identityDto.getSociety().getCode());
//
//            ResponseEntity<InsuranceMaster[]> response = restTemplate.getForEntity(builder.toUriString(), InsuranceMaster[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Insurance fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Insurance fetch", e);
        }
        return null;
    }
}
