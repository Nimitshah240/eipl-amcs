package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.task.ProductSaleRateLoadTask;
import com.eipl.amcs.operation.billing.dto.MilkSummaryDataEntry;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class MilkSummaryDataEntryTask extends Task<List<MilkCollection>> {

    private final String fromDateStr;
    private final String toDateStr;

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkSummaryDataEntryTask.class);

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public MilkSummaryDataEntryTask(String fromDateStr, String toDateStr) {
        this.fromDateStr = fromDateStr;
        this.toDateStr = toDateStr;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, DATE_TIME_FMT);
            LocalDateTime toDate = LocalDateTime.parse(toDateStr, DATE_TIME_FMT);

            MilkCollectionService service =  EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<MilkCollection> collectionResultList = service.findAllBetween(fromDate, toDate);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString())
//                    .queryParam("type", "SUMMARY");
//            ResponseEntity<MilkCollection[]> response = restTemplate.getForEntity(builder.toUriString(), MilkCollection[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("MilkSummaryDataEntry fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
            return collectionResultList;
        } catch (Exception e) {
            LOGGER.error("MilkSummaryDataEntry fetch", e);
        }
        return null;
    }


}
