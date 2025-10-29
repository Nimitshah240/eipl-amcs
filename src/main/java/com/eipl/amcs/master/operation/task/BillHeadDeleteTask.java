package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

/**
 * This class acts as a Delete task for BillHead.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillHeadDeleteTask extends Task<Boolean> {
    private final String code;

    public BillHeadDeleteTask(String code) {
        this.code = code;
    }

    /**
     * Method calls api with endpoint bill-head with method delete to delete the bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected Boolean call() throws Exception {
        try {
            BillHeadService service = EmcsAppContext.getContext().getBean(BillHeadService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILLHEAD + "/{code}";
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
