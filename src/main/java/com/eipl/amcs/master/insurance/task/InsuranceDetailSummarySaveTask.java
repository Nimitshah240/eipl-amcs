package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.dto.InsuranceDetailSummary;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class InsuranceDetailSummarySaveTask extends Task<Object> {

    private InsuranceDetailSummary dto;
    private int process;

    public InsuranceDetailSummarySaveTask(InsuranceDetailSummary dto, int process) {
        this.dto = dto;
        this.process = process;
    }

    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE + "/save-summary";
            ResponseEntity<InsuranceDetailSummary> response = process == 0
                    ? restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), InsuranceDetailSummary.class)
                    : restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), InsuranceDetailSummary.class);

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