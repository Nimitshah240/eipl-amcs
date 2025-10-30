package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.IdentityPayload;
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

import java.util.Map;

import static com.eipl.amcs.utils.AppConstant.UrlPath.LIVE_URL;

public class IdentityCheckTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCheckTask.class);

    private final String societyCode;
    private final String mobileNo;

    public IdentityCheckTask(String societyCode, String mobileNo) {
        this.societyCode = societyCode;
        this.mobileNo = mobileNo;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, LIVE_URL) + AppConstant.UrlPath.IDENTITY_CHECK;
            IdentityPayload payload = new IdentityPayload(mobileNo);
            RealTimeRequest<IdentityPayload> requestPayload = new RealTimeRequest<>(societyCode, "", payload);

            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;

            return respBody.getData();
        } catch (Exception e) {
            LOGGER.error("Register Identity", e);
        }
        return null;
    }
}

