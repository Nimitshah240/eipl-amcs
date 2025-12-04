package com.eipl.amcs.auth.task;

import com.eipl.amcs.base.task.IdentityCheckTask;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCheckTask.class);

    private final String societyCode;
    private final String token;


    public VerificationTask(String societyCode,String token) {
        this.societyCode = societyCode;
        this.token = token;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = "http://qaqc.emilkpro.in/" + AppConstant.UrlPath.VERIFICATION;
            Map<String, Object> identityDataMap = new HashMap<>();
            identityDataMap.put("otpCode", "1234");
            RealTimeRequest<Map<String, Object>> requestPayload = new RealTimeRequest<>(societyCode, token, identityDataMap);
            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;

            LOGGER.info("Verification successful");
            return respBody.getData();
        } catch (Exception e) {
            LOGGER.error("Register Identity", e);
        }
        return null;
    }
}
