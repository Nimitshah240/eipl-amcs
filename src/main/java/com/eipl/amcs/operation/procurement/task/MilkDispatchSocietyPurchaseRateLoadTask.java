package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MilkDispatchSocietyPurchaseRateLoadTask extends Task<SocietyMilkPurchaseRate> {
    private LocalDateTime date;
    private Shift shift;
    private Society society;

    public MilkDispatchSocietyPurchaseRateLoadTask(LocalDateTime date, Shift shift, Society society) {
        this.date = date;
        this.shift = shift;
        this.society = society;
    }

    @Override
    protected SocietyMilkPurchaseRate call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH + "/rate-code";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("date", date.toString())
                    .queryParam("shiftCode", shift.getCode())
                    .queryParam("societyCode", society.getCode());
            ResponseEntity<SocietyMilkPurchaseRate> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, SocietyMilkPurchaseRate.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
