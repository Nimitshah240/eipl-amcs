package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.service.ShareRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShareRateLoadTask extends Task<List<ShareRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareRateLoadTask.class);

    @Override
    protected List<ShareRate> call() throws Exception {
        try {
            ShareRateService service = EmcsAppContext.getContext().getBean(ShareRateService.class);
            List<ShareRate> list = service.findAll();

            if (list == null || list.isEmpty()) return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE_RATE;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
//            ResponseEntity<ShareRate[]> response = restTemplate.getForEntity(builder.toUriString(), ShareRate[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
