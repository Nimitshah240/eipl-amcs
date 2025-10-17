package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerOpeningBalanceLoadTask extends Task<List<LedgerOpeningBalance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerOpeningBalanceLoadTask.class);

    @Override
    protected List<LedgerOpeningBalance> call() throws Exception {
        try {
            LedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(LedgerOpeningBalanceService.class);
            List<LedgerOpeningBalance> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_OPENING_BALANCE;
//            ResponseEntity<LedgerOpeningBalance[]> response = restTemplate.getForEntity(url, LedgerOpeningBalance[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("LedgerOpeningBalance fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("LedgerOpeningBalance fetch", e);
        }
        return null;
    }
}
