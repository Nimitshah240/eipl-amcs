package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FetchAllPaymentCycleLoadTask extends Task<List<SocietyPaymentCycle>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask.class);

    private final LocalDate date;
    private final Integer limit;

    public FetchAllPaymentCycleLoadTask(LocalDate date,Integer limit) {
        this.date = date;
        this.limit = limit;
    }

    @Override
    protected List<SocietyPaymentCycle> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) +
                    AppConstant.UrlPath.SOCIETY_PAYMENT_CYCLE + "/fetchAll";
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("date", date.toString())
                    .queryParam("limit", limit);
            ResponseEntity<SocietyPaymentCycle[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                    HttpMethod.GET, null, SocietyPaymentCycle[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Payment Cycle List Fetch {}", response.getBody());
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Payment Cycle List Fetch ", e);
        }
        return null;
    }
}
