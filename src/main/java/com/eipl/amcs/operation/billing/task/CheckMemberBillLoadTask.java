package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.MemberBill;
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

public class CheckMemberBillLoadTask extends Task<List<MemberBill>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMemberBillLoadTask.class);
    private final SocietyPaymentCycle paymentCycle;

    public CheckMemberBillLoadTask(SocietyPaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    @Override
    protected List<MemberBill> call() throws Exception {
        try {

            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING + "/checkBill";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("paymentCycleCode", paymentCycle.getCode());
            ResponseEntity<MemberBill[]> response = restTemplate.getForEntity(builder.toUriString(), MemberBill[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());

        } catch (Exception e) {
            LOGGER.error("Memberbill fetch", e);
        }
        return null;
    }
}
