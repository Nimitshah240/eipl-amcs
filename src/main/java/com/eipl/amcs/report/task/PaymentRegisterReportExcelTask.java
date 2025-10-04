package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.PaymentForBank;
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

public class PaymentRegisterReportExcelTask extends Task<List<PaymentForBank>> {
    private String societyCode;
    private String societyPaymentCycleCode;
    private String locale;
    private Integer paymentMode;
    private String bankCode;


    public PaymentRegisterReportExcelTask(String societyCode, String societyPaymentCycleCode,String locale,Integer paymentMode,String bankCode) {
        this.societyCode = societyCode;
        this.societyPaymentCycleCode = societyPaymentCycleCode;
        this.locale = locale;
        this.paymentMode = paymentMode;
        this.bankCode = bankCode;
    }

    public PaymentRegisterReportExcelTask() {

    }

    @Override
    protected List<PaymentForBank> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PAYMENT_FOR_BANK_REPORT_EXCEL;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("societyPaymentCycleCode", societyPaymentCycleCode)
                    .queryParam("paymentmode", paymentMode)
                    .queryParam("bankcode", bankCode).queryParam("locale", locale);

            ResponseEntity<PaymentForBank[]> response = restTemplate.getForEntity(builder.toUriString(), PaymentForBank[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
                return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
