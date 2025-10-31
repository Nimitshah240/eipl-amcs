package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InsuranceDetailFetchDeletedTask extends Task<List<InsuranceDetail>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceDetailLoadTask.class);
    private final Integer insuranceMasterCode;

    public InsuranceDetailFetchDeletedTask(Integer insuranceMasterCode) {
        this.insuranceMasterCode = insuranceMasterCode;
    }

    @Override
    protected List<InsuranceDetail> call() throws Exception {
        try {
            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            List<InsuranceDetail> list = service.findDeletedInsuranceDetailByInsuranceMaster(insuranceMasterCode);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("InsuranceDetail fetch", e);
        }
        return null;
    }
}
