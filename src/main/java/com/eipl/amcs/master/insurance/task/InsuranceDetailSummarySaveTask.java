package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class InsuranceDetailSummarySaveTask extends Task<Object> {

    private final InsuranceDetailSummary dto;
    private final int process;

    public InsuranceDetailSummarySaveTask(InsuranceDetailSummary dto, int process) {
        this.dto = dto;
        this.process = process;
    }

    @Override
    protected Object call() throws Exception {
        try {
            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            if (this.process == 0) {
                service.saveDetailsSumamry(dto, CommonUtil.setIdentityHeader());
            } else {
                service.updateInsuranceSummaryDetails(dto, CommonUtil.setIdentityHeader());
            }
            return true;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE + "/save-summary";
//            ResponseEntity<InsuranceDetailSummary> response = process == 0
//                    ? restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), InsuranceDetailSummary.class)
//                    : restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), InsuranceDetailSummary.class);
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
//        catch (HttpStatusCodeException e) {
//            String responseBody = e.getResponseBodyAsString();
//            if (responseBody == null || responseBody.trim().isEmpty()) {
//                return "Server returned an error with no message.";
//            }
//            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class)
//                    .parseJsonString(responseBody);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}