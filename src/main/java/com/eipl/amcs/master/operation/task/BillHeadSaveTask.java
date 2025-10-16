package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * This class acts as a BillHead save task to save or update bill head
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillHeadSaveTask extends Task<Object> {

    private final BillHead billHead;
    private final short update;

    public BillHeadSaveTask(BillHead billHead, short update) {
        this.billHead = billHead;
        this.update = update;
    }


    /**
     * Method calls api with endpoint bill-head with method post or put
     * depending upon to create or update the bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected Object call() throws Exception {
        try {
            BillHeadService service = EmcsAppContext.getContext().getBean(BillHeadService.class);
            if (billHead == null)
                return null;
            if (this.update == 0) {
                service.saveBillHead(billHead, CommonUtil.setIdentityHeader());
            } else {
                service.updateBillHead(billHead, CommonUtil.setIdentityHeader());
            }
            return true;

//
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILLHEAD;
//
//            ResponseEntity<BillHead> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(billHead), BillHead.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(billHead), BillHead.class);
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
