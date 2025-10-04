package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BonusRegisterAllExcelTask extends Task<List<Map<String, Object>>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String memberCode;
    private String locale;
    private Integer paymentMode;
    private Integer bonusType;
    private String bankCode;


    public BonusRegisterAllExcelTask(String societyCode, String memberCode, String locale, Integer paymentMode, String bankCode,
                                     LocalDate fromDate, LocalDate toDate, Integer bonusType) {
        this.societyCode = societyCode;
        this.memberCode = memberCode;
        this.locale = locale;
        this.paymentMode = paymentMode;
        this.bankCode = bankCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.bonusType = bonusType;
    }

    public BonusRegisterAllExcelTask() {

    }

    @Override
    protected List<Map<String, Object>> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS_REPORT_ALL;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("memberCode", memberCode)
                    .queryParam("paymentmode", paymentMode)
                    .queryParam("bankcode", bankCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString())
                    .queryParam("bonusType", bonusType)
                    .queryParam("locale", locale);

            ResponseEntity<Map[]> response = restTemplate.getForEntity(builder.toUriString(), Map[].class);
            if (response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
