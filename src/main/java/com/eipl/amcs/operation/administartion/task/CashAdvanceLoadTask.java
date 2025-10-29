package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.CashAdvance;
import com.eipl.amcs.master.account.service.CashAdvanceService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CashAdvanceLoadTask extends Task<List<CashAdvance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAdvanceLoadTask.class);

    @Override
    protected List<CashAdvance> call() throws Exception {
        try {
            CashAdvanceService service = EmcsAppContext.getContext().getBean(CashAdvanceService.class);
            List<CashAdvance> list = service.findAll();

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.CASHADVANCE;
//            ResponseEntity<CashAdvance[]> response = restTemplate.getForEntity(url, CashAdvance[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("CashAdvance fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
            if (list == null || list.isEmpty()) return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("CashAdvance fetch", e);
        }
        return null;
    }
}
