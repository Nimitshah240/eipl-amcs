package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class FinancialYearLoadTask extends Task<List<FinancialYear>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialYearLoadTask.class);

    @Override
    protected List<FinancialYear> call() throws Exception {
        try {


//            private FinancialYearService financialYearService;
//            List<FinancialYear> list = financialYearService.findAll();




            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.FINANCIAL_YEAR;
            ResponseEntity<FinancialYear[]> response = restTemplate.getForEntity(url, FinancialYear[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("FinancialYear fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("FinancialYear fetch", e);
        }
        return null;
    }
}
