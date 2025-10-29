package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import javafx.concurrent.Task;

import java.util.List;

public class MilkRateBasedLoadTask extends Task<List<MemberMilkPurchaseRateBased>> {
    private final String rateCode;

    public MilkRateBasedLoadTask(String rateCode) {
        this.rateCode = rateCode;
    }

    @Override
    protected List<MemberMilkPurchaseRateBased> call() throws Exception {
        try {

            MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
            List<MemberMilkPurchaseRateBased> list = (service.fetchRateBased(rateCode));

            if (list == null || list.isEmpty()) return null;
            return list;
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_MILK_PURCHASE_RATE + "/based";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code", rateCode);
//
//            ResponseEntity<MemberMilkPurchaseRateBased[]> response = restTemplate.getForEntity(builder.toUriString(), MemberMilkPurchaseRateBased[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
