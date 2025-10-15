package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class LedgerOpeningBalanceLoadTask extends Task<List<LedgerOpeningBalance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerOpeningBalanceLoadTask.class);

    @Override
    protected List<LedgerOpeningBalance> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_OPENING_BALANCE;
            ResponseEntity<LedgerOpeningBalance[]> response = restTemplate.getForEntity(url, LedgerOpeningBalance[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("LedgerOpeningBalance fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("LedgerOpeningBalance fetch", e);
        }
        return null;
    }
}
