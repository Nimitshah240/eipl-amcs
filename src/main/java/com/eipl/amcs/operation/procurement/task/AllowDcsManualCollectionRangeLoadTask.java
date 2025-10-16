package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class AllowDcsManualCollectionRangeLoadTask extends Task<List<AllowDcsManualCollectionRange>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllowDcsManualCollectionRangeLoadTask.class);

    @Override
    protected List<AllowDcsManualCollectionRange> call() throws Exception {
        try {
            AllowDcsManualCollectionRangeRepository repository =  EmcsAppContext.getContext().getBean(AllowDcsManualCollectionRangeRepository.class);;

            List<AllowDcsManualCollectionRange> list = repository.findAll(Sort.by("createdAt").descending());
            for (AllowDcsManualCollectionRange request : list) {
                request.setFromShift(Hibernate.unproxy(request.getFromShift(), Shift.class));
                request.setToShift(Hibernate.unproxy(request.getToShift(), Shift.class));
                request.setSociety(Hibernate.unproxy(request.getSociety(), Society.class));
            }
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ALLOWDCSMANUALCOLLECTIONRANGE;
//            ResponseEntity<AllowDcsManualCollectionRange[]> response = restTemplate.getForEntity(url, AllowDcsManualCollectionRange[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("ManualRequest fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ManualRequest fetch", e);
        }
        return null;
    }
}
