package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class MilkDispatchDeleteTask extends Task<Boolean> {

    private final MilkDispatch dispatch;


    public MilkDispatchDeleteTask(MilkDispatch dispatch) {
        this.dispatch = dispatch;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            service.delete(dispatch.getChallanNo(), CommonUtils.setIdentityHeader());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH + "/delete";
////            Map<String, Object> uriVariables = new HashMap<>();
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code",dispatch.getChallanNo());
//
//            ResponseEntity<Void> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Void.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
