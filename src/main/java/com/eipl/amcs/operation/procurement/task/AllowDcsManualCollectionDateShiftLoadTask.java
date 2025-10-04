package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
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
import java.util.Objects;

public class AllowDcsManualCollectionDateShiftLoadTask extends Task<List<AllowDcsManualCollectionRange>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllowDcsManualCollectionDateShiftLoadTask.class);

    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
    private final Integer selectedIndex;

    public AllowDcsManualCollectionDateShiftLoadTask(LocalDateTime fromDate, LocalDateTime toDate, Integer selectedIndex) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.selectedIndex = selectedIndex;
    }


    @Override
    protected List<AllowDcsManualCollectionRange> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ALLOWDCSMANUALCOLLECTIONRANGE + "/findByDateShift";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString())
                    .queryParam("type", selectedIndex);
            ResponseEntity<AllowDcsManualCollectionRange[]> response = restTemplate.getForEntity(builder.toUriString(), AllowDcsManualCollectionRange[].class);
            if (response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
