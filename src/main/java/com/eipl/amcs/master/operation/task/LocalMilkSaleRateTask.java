package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocalMilkSaleRateTask extends Task<LocalMilkSaleRate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleRateTask.class);

    private final LocalDate date;
    private final Integer milkType, milkClass;

    public LocalMilkSaleRateTask(LocalDate date, Integer milkType, Integer milkClass) {
        this.date = date;
        this.milkType = milkType;
        this.milkClass = milkClass;
    }

    @Override
    protected LocalMilkSaleRate call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.RATE;
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("date", date);
//            uriVariables.put("milkType", milkType);
//            uriVariables.put("milkClass", milkClass);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("date", date.toString())
                    .queryParam("milkType", milkType)
                    .queryParam("milkClass", milkClass);
//            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, null, BigDecimal.class, uriVariables);
            ResponseEntity<LocalMilkSaleRate> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, LocalMilkSaleRate.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("LocalMilkSaleRate fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("LocalMilkSaleRate fetch", e);
        }
        return null;
    }
}
