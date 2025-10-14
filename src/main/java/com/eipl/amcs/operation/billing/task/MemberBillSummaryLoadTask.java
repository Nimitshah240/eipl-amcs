package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class MemberBillSummaryLoadTask extends Task<List<MemberBillSummary>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillSummaryLoadTask.class);

    @Override
    protected List<MemberBillSummary> call() throws Exception {
        try {

            MemberBillService service =  EmcsAppContext.getContext().getBean(MemberBillService.class);
            List<MemberBillSummary> summaryList = service.findMemberBillSummaryBetWeen(MainApp.getFinancialYear().getStartDate(),
                    MainApp.getFinancialYear().getEndDate());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING + "/summary";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", MainApp.getFinancialYear().getStartDate().toString())
//                    .queryParam("toDate", MainApp.getFinancialYear().getEndDate().toString());
//            ResponseEntity<MemberBillSummary[]> response = restTemplate.getForEntity(builder.toUriString(), MemberBillSummary[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
             return summaryList;
        } catch (Exception e) {
            LOGGER.error("Memberbill summary fetch", e);
        }
        return null;
    }
}
