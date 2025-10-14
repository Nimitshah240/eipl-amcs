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
import java.util.Arrays;
import java.util.List;

public class MilkSummaryDataEntryTask extends Task<List<MilkCollection>> {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkSummaryDataEntryTask.class);

    public MilkSummaryDataEntryTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            MilkCollectionService service =  EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            service.findAllBetween(fromDate, toDate);

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
        } catch (Exception e) {
            LOGGER.error("MilkSummaryDataEntry fetch", e);
        }
        return null;
    }


}
