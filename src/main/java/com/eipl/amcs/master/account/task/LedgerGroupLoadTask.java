package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.service.LedgerGroupService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerGroupLoadTask extends Task<List<LedgerGroup>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerGroupLoadTask.class);

    @Override
    protected List<LedgerGroup> call() throws Exception {
        try {
            LedgerGroupService service = EmcsAppContext.getContext().getBean(LedgerGroupService.class);
            List<LedgerGroup> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_GROUP;
//            ResponseEntity<LedgerGroup[]> response = restTemplate.getForEntity(url, LedgerGroup[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("LedgerGroup fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("LedgerGroup fetch", e);
        }
        return null;
    }
}
