package com.eipl.amcs.reportengine.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.service.RptTableResultService;
import javafx.concurrent.Task;

import java.util.List;

public class RptTableResultSaveTask extends Task<List<RptTableResult>> {

    private final List<RptTableResult> rptTableResultList;
    private final short update;

    public RptTableResultSaveTask(List<RptTableResult> rptTableResultList, short update) {
        this.rptTableResultList = rptTableResultList;
        this.update = update;
    }


    @Override
    protected List<RptTableResult> call() throws Exception {
        try {
            RptTableResultService service = EmcsAppContext.getContext().getBean(RptTableResultService.class);
            return service.save(rptTableResultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
