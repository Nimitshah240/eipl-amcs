package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FetchCurrentSocietyPaymentCycleLoadTask extends Task<SocietyPaymentCycle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchCurrentSocietyPaymentCycleLoadTask.class);

    private final Integer code;
    private final LocalDateTime date;

    public FetchCurrentSocietyPaymentCycleLoadTask(Integer code, LocalDateTime date) {
        this.code = code;
        this.date = date;
    }

    @Override
    protected SocietyPaymentCycle call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER + "/fetchspc";
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("code", code);
            uriVariables.put("date", date);
            ResponseEntity<SocietyPaymentCycle> response = restTemplate.exchange(url, HttpMethod.GET, null, SocietyPaymentCycle.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("PaymentCycle fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("PaymentCycle fetch", e);
        }
        return null;
    }
}
