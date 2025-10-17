package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptSummaryDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MilkReceiptSummaryTask extends Task<List<MilkReceiptSummaryDto>> {
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    public MilkReceiptSummaryTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkReceiptSummaryDto> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_RECEIPT + "/milk-receipt-summary";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString());
            ResponseEntity<MilkReceiptSummaryDto[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkReceiptSummaryDto[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
