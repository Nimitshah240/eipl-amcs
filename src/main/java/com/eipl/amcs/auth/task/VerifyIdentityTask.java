package com.eipl.amcs.auth.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.FtpRequestPayload;
import com.eipl.amcs.network.RealTimeResponse;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class VerifyIdentityTask extends Task<String> {

    private String clientCode;

    public VerifyIdentityTask(String clientCode) {
        this.clientCode = clientCode;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyIdentityTask.class);

    @Override
    protected String call() throws Exception {
        try {
            LOGGER.info("Verifying client code : {}", clientCode);
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = "http://amcsapp.emilkpro.in/webservice/eipl/v1/eipl-app/verify-identity";

            FtpRequestPayload payload = new FtpRequestPayload(null, "", clientCode, "", "", "");
            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();

            if (respBody.getData() != null && !respBody.getData().isEmpty()) {
                LOGGER.info("Received base url");
                return String.valueOf(respBody.getData().get("vendorUrl"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("Not received base url");
        return null;
    }
}
