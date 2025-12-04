package com.eipl.amcs.auth.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class VerificationTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationTask.class);

    private final String societyCode;
    private final String token;
    private final String baseUrl;


    public VerificationTask(String societyCode, String token, String baseUrl) {
        this.societyCode = societyCode;
        this.token = token;
        this.baseUrl = baseUrl;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, baseUrl) + AppConstant.UrlPath.VERIFICATION;
            LOGGER.info(url);
            Map<String, Object> identityDataMap = new HashMap<>();
            identityDataMap.put("otpCode", "1234");
            RealTimeRequest<Map<String, Object>> requestPayload = new RealTimeRequest<>(societyCode, token, identityDataMap);
            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;
            LOGGER.info("Verification successful : " + respBody.getData().toString());
            return respBody.getData();
        } catch (Exception e) {
            LOGGER.error("Register Identity", e);
        }
        return null;
    }
}
