package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MemberWiseCollectionDto;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

public class MemberAvgTotalPrevCollectionLoadTask extends Task<Object> {
    private String code;
    private String no;
    private String paymentCycleCode;
    private String milktype;
    private LocalDate date;
    private int shiftCode;

    public MemberAvgTotalPrevCollectionLoadTask(String code, String no, String milktype, LocalDate date,
                                                int shiftCode,String paymentCycleCode) {
        this.code = code;
        this.no = no;
        this.milktype = milktype;
        this.date = date;
        this.shiftCode = shiftCode;
        this.paymentCycleCode = paymentCycleCode;
    }

    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/allinone";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code)
                    .queryParam("no", no)
                    .queryParam("milktype", milktype)
                    .queryParam("paymentCycleCode", paymentCycleCode)
                    .queryParam("shiftCode", shiftCode)
                    .queryParam("date", date.toString());
            ResponseEntity<MemberWiseCollectionDto> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, MemberWiseCollectionDto.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
