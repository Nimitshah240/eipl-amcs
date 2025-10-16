package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
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

public class MilkDispatchSummaryTask extends Task<List<MilkDispatchSummaryDto>> {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public MilkDispatchSummaryTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkDispatchSummaryDto> call() throws Exception {
        try {
            MilkDispatchService service=EmcsAppContext.getContext().getBean(MilkDispatchService.class);;
            List<MilkDispatchSummaryDto>milkDispatchSummaryDtos=service.fetchMilkDispatchSummary(fromDate, toDate);
            if (milkDispatchSummaryDtos==null||milkDispatchSummaryDtos.isEmpty())
                return  null;
                return milkDispatchSummaryDtos;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH + "/milk-dispatch-summary";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString());
//            ResponseEntity<MilkDispatchSummaryDto[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkDispatchSummaryDto[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
