package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import javafx.concurrent.Task;

public class MilkDispatchRateAndDetailsLoadTask extends Task<MilkDispatchRateAndDetailsDto> {
    private final String code;

    public MilkDispatchRateAndDetailsLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected MilkDispatchRateAndDetailsDto call() throws Exception {
        try {
            SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
            return service.fetchRateAndDetails(code);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_MILK_PURCHASE_RATE + "/rate-and-details/{code}";
//            Map<String, String> uriVariable = new HashMap<>();
//            uriVariable.put("code", code);
//            ResponseEntity<MilkDispatchRateAndDetailsDto> response = restTemplate.exchange(url, HttpMethod.GET, null, MilkDispatchRateAndDetailsDto.class, uriVariable);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
