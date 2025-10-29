package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceDetailCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceDetailCodeTask.class);

    public InsuranceDetailCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getIdentity().getSocietyRefCode();
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codes = nextCodeService.getNextCode("InsuranceDetail", "insuranceDetailCode", "VLC" + "-" + code + "-", 1);
            if (codes == null || codes.isEmpty())
                return null;
            return codes;


//            String code = MainApp.identityDto.getIdentity().getSocietyRefCode();
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE + "/fetchInsuranceDetailCode";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("code", code);
//            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Insurance Detail fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Insurance Detail fetched: {}", e);
        }
        return null;
    }

}
