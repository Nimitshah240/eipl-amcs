package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class acts as a Number load task to get the new code for BillCriteria.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillCriteriaNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillCriteriaLoadTask.class);
    private final String society;

    public BillCriteriaNumberLoadTask(String society) {
        this.society = society;
    }

    /**
     * Method calls api with endpoint bill-head/next-code with method get
     * to get the new number for code column of the bill criteria.
     *
     * @return String
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("BillCriteria", "code", society, 3);
            if (code == null || code.isEmpty())
                return null;
            return code;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILL_CRITERIA_NUMBER;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("society", society);
//
//            ResponseEntity<String> response = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null, String.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("billCriteria Number fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("billCriteria Number fetch", e);
        }
        return null;
    }
}
