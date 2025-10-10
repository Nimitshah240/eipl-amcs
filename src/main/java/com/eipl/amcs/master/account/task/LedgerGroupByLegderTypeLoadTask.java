package com.eipl.amcs.master.account.task;


import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class LedgerGroupByLegderTypeLoadTask extends Task<List<LedgerGroup>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateByProductTask.class);

    private final Integer code;
    public LedgerGroupByLegderTypeLoadTask(Integer code) {
        this.code = code;
    }

    @Override
    protected List<LedgerGroup> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) +
                    AppConstant.UrlPath.LEDGER_GROUP+ "/findByLedgerType";
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("ledgerTypeCode", code);
            ResponseEntity<LedgerGroup[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                    HttpMethod.GET, null, LedgerGroup[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("LedgerGroup fetched: {}", response.getBody());
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("LedgerGroup fetch", e);
        }
        return null;
    }
}
