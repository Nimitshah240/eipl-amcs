package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.service.SubLedgerOpeningBalanceService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubLedgerOpeningBalanceLoadTask extends Task<List<SubLedgerOpeningBalance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerOpeningBalanceLoadTask.class);

    @Override
    protected List<SubLedgerOpeningBalance> call() throws Exception {
        try {
            SubLedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(SubLedgerOpeningBalanceService.class);
            List<SubLedgerOpeningBalance> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_OPENING_BALANCE;
//            ResponseEntity<SubLedgerOpeningBalance[]> response = restTemplate.getForEntity(url, SubLedgerOpeningBalance[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("SubLedgerOpeningBalance fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("SubLedgerOpeningBalance fetch", e);
        }
        return null;
    }
}
