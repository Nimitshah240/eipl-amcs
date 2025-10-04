package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.MilkDispatchChallan;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class MilkDispatchChallanTask extends Task<List<MilkDispatchChallan>> {

    private String challanNo;

    public MilkDispatchChallanTask(String challanNo) {
        this.challanNo = challanNo;
    }

    @Override
    protected List<MilkDispatchChallan> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);

            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.REPORT_MILK_CHALLAN;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", MainApp.identityDto.getSociety().getCode())
                    .queryParam("challanNo", challanNo);
            ResponseEntity<MilkDispatchChallan[]> response = restTemplate.getForEntity(builder.toUriString(), MilkDispatchChallan[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
