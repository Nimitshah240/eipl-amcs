package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.service.LedgerMappingTaxDetailService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class LedgerMappingTaxDetailSaveTask extends Task<Object> {

    private final List<LedgerMappingTaxDetail> dto;

    public LedgerMappingTaxDetailSaveTask(List<LedgerMappingTaxDetail> dto) {
        this.dto = dto;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerMappingTaxDetailService service = EmcsAppContext.getContext().getBean(LedgerMappingTaxDetailService.class);
            service.save(dto, CommonUtils.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_TAX_DETAIL;
//
//            ResponseEntity<String> response =
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), String.class);
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
