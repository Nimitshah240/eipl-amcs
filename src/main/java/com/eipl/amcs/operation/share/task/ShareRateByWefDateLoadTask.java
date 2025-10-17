package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.service.ShareRateService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

public class ShareRateByWefDateLoadTask extends Task<ShareRate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShareRateByWefDateLoadTask.class);

    private final LocalDate date;

    public ShareRateByWefDateLoadTask(LocalDate date) {
        this.date = date;
    }

    @Override
    protected ShareRate call() throws Exception {
        try {

            ShareRateService service=EmcsAppContext.getContext().getBean(ShareRateService.class);;
            return service.fetchRate(date);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE_RATE+"/rate";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("date", date.toString());
//            ResponseEntity<ShareRate> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, ShareRate.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK) return null;
//            LOGGER.info("ShareRate fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ShareRate fetch", e);
        }
        return null;
    }
}
