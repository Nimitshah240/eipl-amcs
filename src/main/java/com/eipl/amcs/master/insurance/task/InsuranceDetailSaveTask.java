package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
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

public class InsuranceDetailSaveTask extends Task<Object> {

    private InsuranceDetail dto;
    private int process;

    public InsuranceDetailSaveTask(InsuranceDetail dto, int process) {
        this.dto = dto;
        this.process = process;
    }

    @Override
    protected Object call() throws Exception {
        try {
            InsuranceMasterService service= EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            if (this.process == 0) {
                service.saveDetails(dto, CommonUtil.setIdentityHeader());
            }else {
                service.updateDetails(dto, CommonUtil.setIdentityHeader());
            }
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE + "/detail";
//            ResponseEntity<InsuranceDetail> response = process == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), InsuranceDetail.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), InsuranceDetail.class);
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
