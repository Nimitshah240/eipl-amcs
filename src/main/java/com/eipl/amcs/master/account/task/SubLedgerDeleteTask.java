package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;

public class SubLedgerDeleteTask extends Task<Boolean> {
    private final String code;

    public SubLedgerDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            SubLedgerService service = EmcsAppContext.getContext().getBean(SubLedgerService.class);
            service.delete(code, CommonUtil.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//
//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
