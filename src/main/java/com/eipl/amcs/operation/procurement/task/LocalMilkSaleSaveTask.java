package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class LocalMilkSaleSaveTask extends Task<Object> {
    private final LocalMilkSale dto;
    private final short update;

    public LocalMilkSaleSaveTask(LocalMilkSale dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            LocalMilkSaleService service=EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE;
//            String url;
//            ResponseEntity<LocalMilkSale> response = this.update == 0 ?
//            ResponseEntity<LocalMilkSale> response;
                    if(this.update == 0) {
//                        url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE;
//                        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), LocalMilkSale.class);
                        service.save(dto, CommonUtil.setIdentityHeader());
                    }else {
//                        url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE + "/{code}";
//                        Map<String, Object> uriVariables = new HashMap<>();
//                        uriVariables.put("code", dto.getCode());
//                        response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), LocalMilkSale.class, uriVariables);
                        service.update(dto, CommonUtil.setIdentityHeader());
                    }
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
