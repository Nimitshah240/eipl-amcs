package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.DairySaleRegister;
import com.eipl.amcs.report.dto.SocietyPurchase;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SocietyPurchaseTask extends Task<List<SocietyPurchase>> {
    private String societyCode;
    private LocalDateTime from_date;
    private LocalDateTime to_date;


    public SocietyPurchaseTask(String societyCode, LocalDateTime fromDate, LocalDateTime toDate) {
        this.societyCode = societyCode;
        this.from_date = fromDate;
        this.to_date = toDate;

    }

    public SocietyPurchaseTask() {

    }

    @Override
    protected List<SocietyPurchase> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_PURCHASE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", from_date.toString())
                    .queryParam("toDate", to_date.toString());


            ResponseEntity<SocietyPurchase[]> response = restTemplate.getForEntity(builder.toUriString(), SocietyPurchase[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
