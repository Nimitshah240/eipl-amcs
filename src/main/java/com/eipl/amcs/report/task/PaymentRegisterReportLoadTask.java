package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.PaymentRegisterForCash;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class PaymentRegisterReportLoadTask extends Task<List<PaymentRegisterForCash>> {
    private final String societyCode;
    private final String societyPaymentCycleCode;
    private final Integer paymentMode;

    public PaymentRegisterReportLoadTask(String societyCode, String societyPaymentCycleCode, Integer paymentMode) {
        this.societyCode = societyCode;
        this.societyPaymentCycleCode = societyPaymentCycleCode;
        this.paymentMode = paymentMode;
    }

    @Override
    protected List<PaymentRegisterForCash> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PAYMENT_REGISTER;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("paymentMode", paymentMode)
                    .queryParam("societyPaymentCycleCode", societyPaymentCycleCode);
            ResponseEntity<PaymentRegisterForCash[]> response = restTemplate.getForEntity(builder.toUriString(), PaymentRegisterForCash[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
