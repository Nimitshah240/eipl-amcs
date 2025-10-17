package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.math.BigDecimal;

public class MemberTotalAmountLoadTask extends Task<BigDecimal> {
    private final String societyPaymentCycleCode;
    private final String memberCode;

    public MemberTotalAmountLoadTask(String societyPaymentCycleCode, String memberCode) {
        this.societyPaymentCycleCode = societyPaymentCycleCode;
        this.memberCode = memberCode;
    }

    @Override
    protected BigDecimal call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            BigDecimal response = service.findTotalAmount(societyPaymentCycleCode, memberCode);
            return response;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/total_amount";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("societyPaymentCycleCode", societyPaymentCycleCode)
//                    .queryParam("memberCode", memberCode);
//            ResponseEntity<BigDecimal> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
//                    null, BigDecimal.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
