package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            return service.findInsuranceDetailSummaryByInsuranceMaster(insuranceMasterCode);

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
