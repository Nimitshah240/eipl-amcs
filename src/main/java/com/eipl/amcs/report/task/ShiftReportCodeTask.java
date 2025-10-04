package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.report.dto.ShiftReportCode;
import com.eipl.amcs.utils.AppConstant;
import com.sun.tools.javac.Main;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class ShiftReportCodeTask extends Task<List<ShiftReportCode>> {
    private String societyCode;
    private LocalDateTime collectionDateTime;

    public ShiftReportCodeTask(String societyCode, LocalDateTime collectionDateTime) {
        this.societyCode = societyCode;
        this.collectionDateTime = collectionDateTime;
    }

    @Override
    protected List<ShiftReportCode> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.REPORT_SHIFT_CODE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("collectionDateTime", collectionDateTime.toString());
            ResponseEntity<ShiftReportCode[]> response = restTemplate.getForEntity(builder.toUriString(), ShiftReportCode[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
