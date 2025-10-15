package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class SubLedgerOpeningBalanceSaveTask extends Task<Object> {

    private final SubLedgerOpeningBalance dto;
    private final short update;

    public SubLedgerOpeningBalanceSaveTask(SubLedgerOpeningBalance dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_OPENING_BALANCE;

            ResponseEntity<SubLedgerOpeningBalance> response = this.update == 0 ?
                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), SubLedgerOpeningBalance.class) :
                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), SubLedgerOpeningBalance.class);

            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                return null;
            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
