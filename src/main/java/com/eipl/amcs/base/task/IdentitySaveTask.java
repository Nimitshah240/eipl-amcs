package com.eipl.amcs.base.task;

import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.base.service.IdentityDetailsService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class IdentitySaveTask extends Task<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentitySaveTask.class);

    private final Identity dto;

    public IdentitySaveTask(Identity dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
//            FOR VERIFICATION AND TO GET SYNCKEY
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = AppConstant.baseUrlRealTime + AppConstant.UrlPath.VERIFICATION;
            LOGGER.info(url);
            Map<String, Object> identityDataMap = new HashMap<>();
            identityDataMap.put("otpCode", "1234");
            RealTimeRequest<Map<String, Object>> requestPayload = new RealTimeRequest<>(dto.getSocietyCode(), dto.getToken(), identityDataMap);
            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();
            LOGGER.info("Verification successful : " + respBody.getData().toString());
            String syncKey = respBody.getData().get("syncKey").toString();

            dto.setxCol1(syncKey);

            IdentityDetailsService service = EmcsAppContext.getContext().getBean(IdentityDetailsService.class);
            service.save(dto, CommonUtils.setIdentityHeader());
            LOGGER.info("Identity Saved Successful");
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("Identity Saved Failed");
        return null;
    }
}
