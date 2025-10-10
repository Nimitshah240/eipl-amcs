package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class SubLedgerLoadTask extends Task<List<SubLedger>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerLoadTask.class);

    @Override
    protected List<SubLedger> call() throws Exception {
        try {


//            private SubLedgerService subLedgerService;
//            List<SubLedger> list = subLedgerService.findAll();


            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER;
            ResponseEntity<SubLedger[]> response = restTemplate.getForEntity(url, SubLedger[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("SubLedger fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("SubLedger fetch", e);
        }
        return null;
    }
}
