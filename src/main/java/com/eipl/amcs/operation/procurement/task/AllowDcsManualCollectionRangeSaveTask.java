package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;

public class AllowDcsManualCollectionRangeSaveTask extends Task<Object> {
    private final AllowDcsManualCollectionRange dto;
    private final short update;

    public AllowDcsManualCollectionRangeSaveTask(AllowDcsManualCollectionRange dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            AllowDcsManualCollectionRange allowDcsManualCollectionRange = null;
            NextCodeRepository nextCodeRepository = EmcsAppContext.getContext().getBean(NextCodeRepository.class);
            AllowDcsManualCollectionRangeRepository repository = EmcsAppContext.getContext().getBean(AllowDcsManualCollectionRangeRepository.class);

            if (this.update == 0) {
                allowDcsManualCollectionRange.setInitData();
                String nextCode = nextCodeRepository.getNextCode("AllowDcsManualCollectionRange", "code", allowDcsManualCollectionRange.getSociety().getCode(), 3);
                allowDcsManualCollectionRange.setCode(Long.valueOf(nextCode));
                repository.customSave(allowDcsManualCollectionRange, CommonUtil.setIdentityHeader());
            } else {
                allowDcsManualCollectionRange.setupdateData();
                if (allowDcsManualCollectionRange.getStatus() == 1) {
                    allowDcsManualCollectionRange.setCancelledAt(LocalDateTime.now());
                    allowDcsManualCollectionRange.setStatus(3);
                    allowDcsManualCollectionRange.setCancelledBy("SYSTEM");
                }
                repository.customUpdate(allowDcsManualCollectionRange, CommonUtil.setIdentityHeader());
            }


            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ALLOWDCSMANUALCOLLECTIONRANGE;
//
//            ResponseEntity<AllowDcsManualCollectionRange> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), AllowDcsManualCollectionRange.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), AllowDcsManualCollectionRange.class);
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
