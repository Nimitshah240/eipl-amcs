package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
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

public class IdentityCheckTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCheckTask.class);

    private final String societyCode;
    private final String baseUrl;

    public IdentityCheckTask(String societyCode, String baseUrl) {
        this.societyCode = societyCode;
        this.baseUrl = baseUrl;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            SocietyRepository societyRepository = EmcsAppContext.getContext().getBean(SocietyRepository.class);
            Society society = societyRepository.findById(societyCode).orElse(null);
            MainApp.identityDto = new IdentityDto();
            MainApp.identityDto.setSociety(society);
            if (society == null)
                return null;
            String mobileNo = society.getContactPersonMobileNo();

            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, baseUrl) + AppConstant.UrlPath.IDENTITY_CHECK;
            LOGGER.info(url);
            IdentityPayload payload = new IdentityPayload(mobileNo);
            RealTimeRequest<IdentityPayload> requestPayload = new RealTimeRequest<>(societyCode, "", payload);

            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;
            LOGGER.info("Token received :" + respBody.getData());
            return respBody.getData();
        } catch (Exception e) {
            LOGGER.error("Register Identity", e);
        }
        return null;
    }
}

