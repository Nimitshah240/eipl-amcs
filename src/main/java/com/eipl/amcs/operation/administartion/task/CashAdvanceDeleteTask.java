package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.CashAdvanceService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;

public class CashAdvanceDeleteTask extends Task<Boolean> {
    private final String code;

    public CashAdvanceDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            CashAdvanceService service = EmcsAppContext.getContext().getBean(CashAdvanceService.class);
            service.delete(code, CommonUtil.setIdentityHeader());

            return true;
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.CASHADVANCE + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//
//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
