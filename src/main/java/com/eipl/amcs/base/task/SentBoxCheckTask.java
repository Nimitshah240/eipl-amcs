package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.IdentityPayload;
import com.eipl.amcs.network.RealTimeRequest;
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

public class SentBoxCheckTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SentBoxCheckTask.class);

    private final String societyCode;
    private final String mobileNo;

    public SentBoxCheckTask(String societyCode, String mobileNo) {
        this.societyCode = societyCode;
        this.mobileNo = mobileNo;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, "") + AppConstant.UrlPath.SENT_BOX_CHECK;

            IdentityPayload payload = new IdentityPayload();
            RealTimeRequest<IdentityPayload> requestPayload = new RealTimeRequest<>(societyCode, MainApp.identityDto.getIdentity().getToken(), payload);
            requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), Map.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            Map respBody = response.getBody();
            return respBody;
        } catch (Exception e) {
            LOGGER.error("Notification", e);
        }
        return null;
    }
}

