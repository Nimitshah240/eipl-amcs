package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.service.LedgerMappingProductGroupService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class LedgerMappingProductGroupSaveTask extends Task<Object> {

    private final List<LedgerMappingProductGroup> dto;

    public LedgerMappingProductGroupSaveTask(List<LedgerMappingProductGroup> dto) {
        this.dto = dto;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerMappingProductGroupService service = EmcsAppContext.getContext().getBean(LedgerMappingProductGroupService.class);
            service.save(dto, CommonUtil.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_PRODUCT_GROUP;
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
