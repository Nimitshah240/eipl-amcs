package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.service.HamletService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HamletLoadTask extends Task<List<Hamlet>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HamletLoadTask.class);
    private Village village;

    public HamletLoadTask() {

    }

    public HamletLoadTask(Village village) {
        this.village = village;
    }

    @Override
    protected List<Hamlet> call() throws Exception {
        try {
            HamletService service = EmcsAppContext.getContext().getBean(HamletService.class);
            List<Hamlet> list;
            if (village == null)
                list = service.findAll();
            else
                list = service.findAll(village.getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.HAMLET;
//            ResponseEntity<Hamlet[]> response;
//            if (village != null) {
//                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                        .queryParam("villageCode", village.getCode());
//                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Hamlet[].class);
//            } else {
//                response = restTemplate.getForEntity(url, Hamlet[].class);
//            }
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Hamlets fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Hamlets fetch", e);
        }
        return null;
    }
}

