package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MilkSummaryDataEntryTask extends Task<List<MilkCollection>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkSummaryDataEntryTask.class);
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    public MilkSummaryDataEntryTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
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
            if (collectionResultList == null || collectionResultList.isEmpty()) return null;
            return collectionResultList;
        } catch (Exception e) {
            LOGGER.error("MilkSummaryDataEntry fetch", e);
        }
        return null;
    }


}
