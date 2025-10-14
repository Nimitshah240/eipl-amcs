package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.operation.billing.model.MemberBill;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MemberBillLoadTask extends Task<List<MemberBill>> {
    private SocietyPaymentCycle paymentCycle;
    private short generate;
    private Society society;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillLoadTask.class);

    public MemberBillLoadTask(SocietyPaymentCycle paymentCycle, Society society, LocalDateTime fromDate, LocalDateTime toDate, short generate) {
        this.paymentCycle = paymentCycle;
        this.generate = generate;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.society = society;
    }

    @Override
    protected List<MemberBill> call() throws Exception {
        try {
            SocietyPaymentCycleService paymentCycleService = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            List<MemberBill> memberListResult;
            if (generate == 0) {
                memberListResult = service.fetchTableData(paymentCycle);
            }else{
                paymentCycleService.findById(paymentCycle.getCode());
                SocietyPaymentCycle prevPaymentCycle = paymentCycleService.fetchCurrentPaymentCycle(paymentCycle.getFromDate().minusDays(3), null);
                memberListResult = service.findMemberBill(society.getCode(), paymentCycle, prevPaymentCycle);
            }

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            if (generate == 0) {
//                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING + "/fetchBill";
//                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                        .queryParam("paymentCycleCode", paymentCycle.getCode());
//                ResponseEntity<MemberBill[]> response = restTemplate.getForEntity(builder.toUriString(), MemberBill[].class);
//                if (response == null || response.getStatusCode() != HttpStatus.OK)
//                    return null;
//                return Arrays.asList(response.getBody());
//            } else {
//                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING + "/bill";
//                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                        .queryParam("paymentCycleCode", paymentCycle.getCode())
//                        .queryParam("societyCode", society.getCode())
//                        .queryParam("fromDate", fromDate.toString())
//                        .queryParam("toDate", toDate.toString())
//                        .queryParam("generate", generate);
//                ResponseEntity<MemberBill[]> response = restTemplate.getForEntity(builder.toUriString(), MemberBill[].class);
//                if (response == null || response.getStatusCode() != HttpStatus.OK)
//                    return null;
//                return Arrays.asList(response.getBody());
//            }
            return memberListResult;
        } catch (Exception e) {
            LOGGER.error("Memberbill fetch", e);
        }
        return null;
    }
}
