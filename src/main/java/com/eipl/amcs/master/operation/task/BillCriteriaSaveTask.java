package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BillCriteriaSaveTask extends Task<Object> {

    private final BillCriteria billCriteria;
    private final short update;

    public BillCriteriaSaveTask(BillCriteria billCriteria, short update) {
        this.billCriteria = billCriteria;
        this.update = update;
    }

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
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
