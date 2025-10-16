package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import com.eipl.amcs.operation.billing.repository.MemberBillRepository;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberBillTransactionLoadTask extends Task<List<MemberBillTransaction>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillTransactionLoadTask.class);

    private final String code;

    public MemberBillTransactionLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<MemberBillTransaction> call() throws Exception {
        try {

            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            MemberBillRepository repository = EmcsAppContext.getContext().getBean(MemberBillRepository.class);
            MemberBill mb = repository.findById(code).get();
            List<MemberBillTransaction> memberListResult = service.findMemberBillTransaction(mb);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) +
//                    AppConstant.UrlPath.MEMBER_BILLING + "/transaction";
//            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("code", code);
//            ResponseEntity<MemberBillTransaction[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
//                    HttpMethod.GET, null, MemberBillTransaction[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Transaction fetched: {}", response.getBody());
//            return Arrays.asList(response.getBody());
            if (memberListResult == null || memberListResult.isEmpty()) return null;
            return memberListResult;
        } catch (Exception e) {
            LOGGER.error("Transaction fetch", e);
        }
        return null;
    }
}
