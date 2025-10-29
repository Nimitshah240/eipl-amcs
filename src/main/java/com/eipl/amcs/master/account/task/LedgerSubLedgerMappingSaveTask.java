package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class LedgerSubLedgerMappingSaveTask extends Task<Object> {

    private final List<LedgerSubLedgerMapping> dto;
    private final short update;

    public LedgerSubLedgerMappingSaveTask(List<LedgerSubLedgerMapping> dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerService service = EmcsAppContext.getContext().getBean(LedgerService.class);
            if (update == 0) {
                service.save(dto, CommonUtils.setIdentityHeader());
            }
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER+"/mapping";
//
//            ResponseEntity<LedgerSubLedgerMapping[]> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), LedgerSubLedgerMapping[].class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), LedgerSubLedgerMapping[].class);
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
