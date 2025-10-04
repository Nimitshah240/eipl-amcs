package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

public class MilkDispatchPrevRecordGetTask extends Task<MilkDispatch> {
    private LocalDateTime fromDate;

    public MilkDispatchPrevRecordGetTask(LocalDateTime fromDate) {
        this.fromDate = fromDate;

    }

    @Override
    protected MilkDispatch call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH + "/prev-record";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate", fromDate.toString());
            ResponseEntity<MilkDispatch> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkDispatch.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
