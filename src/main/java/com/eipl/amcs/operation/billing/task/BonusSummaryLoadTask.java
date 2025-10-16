package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.service.BonusService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BonusSummaryLoadTask extends Task<List<BonusSummary>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusSummaryLoadTask.class);

    @Override
    protected List<BonusSummary> call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            List<BonusSummary> summaryList = service.findBonusSummaryBetWeen();

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS + "/summary";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
//            ResponseEntity<BonusSummary[]> response = restTemplate.getForEntity(builder.toUriString(), BonusSummary[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
            if (summaryList == null || summaryList.isEmpty()) return null;
            return summaryList;
        } catch (Exception e) {
            LOGGER.error("Memberbill summary fetch", e);
        }
        return null;
    }
}
