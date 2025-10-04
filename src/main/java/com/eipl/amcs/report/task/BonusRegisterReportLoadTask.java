package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.report.dto.BonusRegister;
import com.eipl.amcs.report.dto.PaymentRegisterForCash;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class BonusRegisterReportLoadTask extends Task<List<BonusRegister>> {
    private String societyCode;
    private String bonusSummaryCode;


    public BonusRegisterReportLoadTask(String societyCode, String bonusSummaryCode) {
        this.societyCode = societyCode;
        this.bonusSummaryCode = bonusSummaryCode;
    }

    @Override
    protected List<BonusRegister> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS_REGISTER;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("bonusSummaryCode", bonusSummaryCode);
            ResponseEntity<BonusRegister[]> response = restTemplate.getForEntity(builder.toUriString(), BonusRegister[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
