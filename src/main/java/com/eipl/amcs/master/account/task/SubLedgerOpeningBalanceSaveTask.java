package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.service.SubLedgerOpeningBalanceService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

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
            SubLedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(SubLedgerOpeningBalanceService.class);
            if (update == 0) {
                service.save(dto, CommonUtil.setIdentityHeader());
            } else {
                service.update(dto, CommonUtil.setIdentityHeader());
            }
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_OPENING_BALANCE;
//
//            ResponseEntity<SubLedgerOpeningBalance> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), SubLedgerOpeningBalance.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), SubLedgerOpeningBalance.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
