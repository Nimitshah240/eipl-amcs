package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class InsuranceDetailSaveTask extends Task<Object> {

    private final InsuranceDetail dto;
    private final int process;

    public InsuranceDetailSaveTask(InsuranceDetail dto, int process) {
        this.dto = dto;
        this.process = process;
    }

    @Override
    protected Object call() throws Exception {
        try {
            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            if (this.process == 0) {
                service.saveDetails(dto, CommonUtils.setIdentityHeader());
            } else {
                service.updateDetails(dto, CommonUtils.setIdentityHeader());
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
