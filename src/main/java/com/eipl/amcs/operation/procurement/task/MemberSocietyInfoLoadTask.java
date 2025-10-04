package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MemberSocietyInfoDto;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MemberSocietyInfoLoadTask extends Task<Object> {
    private String code;
    private LocalDateTime date;
    private Integer count;
    private String paymentCycleCode;

    public MemberSocietyInfoLoadTask(String code, LocalDateTime date) {
        this.code = code;
        this.date = date;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPaymentCycleCode(String paymentCycleCode) {
        this.paymentCycleCode = paymentCycleCode;
    }

    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER + "/member-information";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code)
                    .queryParam("count", count == null ? 5 : count)
                    .queryParam("paymentCycle", paymentCycleCode)
                    .queryParam("date", date.toString());

            ResponseEntity<MemberSocietyInfoDto> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, MemberSocietyInfoDto.class);
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
