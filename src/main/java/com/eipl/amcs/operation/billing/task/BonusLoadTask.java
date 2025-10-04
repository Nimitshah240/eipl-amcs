package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BonusLoadTask extends Task<List<Bonus>> {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Integer milkType;
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusLoadTask.class);

    public BonusLoadTask(LocalDateTime fromDate, LocalDateTime toDate,Integer milkType) {
        this.fromDate = fromDate;
        this.milkType = milkType;
        this.toDate = toDate;
    }

    @Override
    protected List<Bonus> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS + "/loaddata";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("milkType", milkType)
                    .queryParam("toDate", toDate.toString());
            ResponseEntity<Bonus[]> response = restTemplate.getForEntity(builder.toUriString(), Bonus[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
