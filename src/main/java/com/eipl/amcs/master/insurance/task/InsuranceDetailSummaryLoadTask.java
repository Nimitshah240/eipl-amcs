package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class InsuranceDetailSummaryLoadTask extends Task<InsuranceDetailSummary> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceDetailSummaryLoadTask.class);

    Integer insuranceMasterCode;

    public InsuranceDetailSummaryLoadTask(Integer insuranceMasterCode) {
        this.insuranceMasterCode = insuranceMasterCode;
    }

    @Override
    protected InsuranceDetailSummary call() throws Exception {
        try {
            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            InsuranceDetailSummary list = service.findInsuranceDetailSummaryByInsuranceMaster(insuranceMasterCode);
            if (list == null)
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE + "/detailSummary";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("insuranceMasterCode", insuranceMasterCode);
//
//            ResponseEntity<InsuranceDetailSummary> response = restTemplate.getForEntity(builder.toUriString(), InsuranceDetailSummary.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("InsuranceDetailSummary fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("InsuranceDetailSummary fetch", e);
        }
        return null;
    }
}
