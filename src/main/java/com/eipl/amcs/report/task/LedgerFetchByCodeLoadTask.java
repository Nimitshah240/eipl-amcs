package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class LedgerFetchByCodeLoadTask extends Task<Ledger> {
    private String ledgerCode;

    public LedgerFetchByCodeLoadTask(String ledgerCode) {
        this.ledgerCode = ledgerCode;

    }

    public LedgerFetchByCodeLoadTask() {

    }

    @Override
    protected Ledger call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER+"/ledger_fetch_by_code";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("ledgerCode", ledgerCode);
            ResponseEntity<Ledger> response = restTemplate.getForEntity(builder.toUriString(), Ledger.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}