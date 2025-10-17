package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

public class MilkCollectionPreRequisiteTask extends Task<MilkCollectionPreReqDto> {
    private final LocalDateTime date;
    private final Shift shift;
    private final Society society;

    public MilkCollectionPreRequisiteTask(LocalDateTime date, Shift shift, Society society) {
        this.date = date;
        this.shift = shift;
        this.society = society;
    }

    @Override
    protected MilkCollectionPreReqDto call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/pre-req";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("date", date.toString())
                    .queryParam("shiftCode", shift.getCode())
                    .queryParam("societyCode", society.getCode());
            ResponseEntity<MilkCollectionPreReqDto> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkCollectionPreReqDto.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
