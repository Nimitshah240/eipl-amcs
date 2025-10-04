package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class acts as a Number load task for BillHead's new code.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillHeadNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillHeadNumberLoadTask.class);
    private final String society;

    public BillHeadNumberLoadTask(String society) {
        this.society = society;
    }

    /**
     * Method calls api with endpoint bill-head/next-code with method get
     * to get the new number for code column of the bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected String call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILL_HEAD_NUMBER;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("society", society);

            ResponseEntity<String> response = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null, String.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("billHead Number fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("billHead Number fetch", e);
        }
        return null;
    }
}
