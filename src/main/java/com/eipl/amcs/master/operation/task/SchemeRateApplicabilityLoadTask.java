package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import com.eipl.amcs.master.operation.repository.SchemeRateApplicabilityRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SchemeRateApplicabilityLoadTask extends Task<List<SchemeRateApplicability>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemeRateApplicabilityLoadTask.class);

    /**
     * Method use to get SchemeRateApplicability from the backend.
     *
     * @return List<SchemeRateApplicability>
     * @author Nimit Shah
     * @createdOn 23-07-2025
     */
    @Override
    protected List<SchemeRateApplicability> call() throws Exception {
        try {
            SchemeRateApplicabilityRepository schemeRateApplicabilityRepository = EmcsAppContext.getContext().getBean(SchemeRateApplicabilityRepository.class);
            List<SchemeRateApplicability> list = schemeRateApplicabilityRepository.findByIsActiveTrue();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SCHEME_RATE_APPLICABILITY;
//            ResponseEntity<SchemeRateApplicability[]> response = restTemplate.getForEntity(url, SchemeRateApplicability[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("SchemeRate fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("SchemeRate error", e);
        }
        return null;
    }
}