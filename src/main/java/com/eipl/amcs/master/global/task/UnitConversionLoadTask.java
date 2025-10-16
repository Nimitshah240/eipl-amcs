package com.eipl.amcs.master.global.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.UnitConversion;
import com.eipl.amcs.master.global.service.UnitConversionService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class UnitConversionLoadTask extends Task<List<UnitConversion>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitConversionLoadTask.class);

    @Override
    protected List<UnitConversion> call() throws Exception {
        try {
            UnitConversionService service= EmcsAppContext.getContext().getBean(UnitConversionService.class);
            List<UnitConversion> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.UNITCONVERSION;
//            ResponseEntity<UnitConversion[]> response = restTemplate.getForEntity(url, UnitConversion[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("UnitConversions fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("UnitConversions fetch", e);
        }
        return null;
    }
}

