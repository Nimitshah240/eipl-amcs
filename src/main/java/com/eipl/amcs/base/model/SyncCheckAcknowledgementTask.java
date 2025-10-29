package com.eipl.amcs.base.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.network.SyncPayloadForAcknowledgement;
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

public class SyncCheckAcknowledgementTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCheckAcknowledgementTask.class);

    private final String forceSyncRequestCode;

    public SyncCheckAcknowledgementTask(String forceSyncRequestCode) {
        this.forceSyncRequestCode = forceSyncRequestCode;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, "") + AppConstant.UrlPath.SYNC_CHECK_ACK;

            SyncPayloadForAcknowledgement payload = new SyncPayloadForAcknowledgement(forceSyncRequestCode);
            RealTimeRequest<SyncPayloadForAcknowledgement> requestPayload = new RealTimeRequest<>(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getIdentity().getToken(), payload);
            requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;

            return response.getBody().getData();
        } catch (Exception e) {
            LOGGER.error("Notification", e);
        }
        return null;
    }
}

