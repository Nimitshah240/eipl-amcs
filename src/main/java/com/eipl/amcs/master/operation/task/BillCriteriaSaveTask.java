package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * This class acts as a save task to save or update BillCriteria.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillCriteriaSaveTask extends Task<Object> {

    private final BillCriteria billCriteria;
    private final short update;

    public BillCriteriaSaveTask(BillCriteria billCriteria, short update) {
        this.billCriteria = billCriteria;
        this.update = update;
    }

    /**
     * Method calls api with endpoint bill-criteria with method post or put
     * depending upon to create or update the bill criteria.
     *
     * @return Object
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected Object call() throws Exception {
        try {
            BillCriteriaService service = EmcsAppContext.getContext().getBean(BillCriteriaService.class);
            if (billCriteria == null)
                return null;
            if (this.update == 0) {
                service.saveBillCriteria(billCriteria, CommonUtils.setIdentityHeader());
            } else {
                service.updateBillCriteria(billCriteria, CommonUtils.setIdentityHeader());
            }
            return true;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILLCRITERIA;
//
//            ResponseEntity<BillCriteria> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(billCriteria), BillCriteria.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(billCriteria), BillCriteria.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
