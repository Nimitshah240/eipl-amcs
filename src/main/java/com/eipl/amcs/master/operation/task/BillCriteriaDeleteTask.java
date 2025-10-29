package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

/**
 * This class acts as a Delete task to delete BillCriteria.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillCriteriaDeleteTask extends Task<Boolean> {
    private final String code;

    public BillCriteriaDeleteTask(String code) {
        this.code = code;
    }

    /**
     * Method calls api with endpoint bill-criteria with method delete to delete the bill criteria.
     *
     * @return Boolean
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected Boolean call() throws Exception {
        try {
            BillCriteriaService service = EmcsAppContext.getContext().getBean(BillCriteriaService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILLCRITERIA + "/{code}";
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
