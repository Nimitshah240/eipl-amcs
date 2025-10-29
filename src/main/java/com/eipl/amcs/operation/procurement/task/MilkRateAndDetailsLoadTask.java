package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import javafx.concurrent.Task;

public class MilkRateAndDetailsLoadTask extends Task<MilkRateAndDetailsDto> {
    private final String code;

    public MilkRateAndDetailsLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected MilkRateAndDetailsDto call() throws Exception {
        try {

            MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
            return service.fetchRateAndDetails(code);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_MILK_PURCHASE_RATE + "/rate-and-details/{code}";
//            Map<String, String> uriVariable = new HashMap<>();
//            uriVariable.put("code", code);
//            ResponseEntity<MilkRateAndDetailsDto> response = restTemplate.exchange(url, HttpMethod.GET, null, MilkRateAndDetailsDto.class, uriVariable);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
