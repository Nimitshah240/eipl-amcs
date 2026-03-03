package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BillHeadLoadTask extends Task<List<BillHead>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillHeadLoadTask.class);
    private final String billHeadFor;

    public BillHeadLoadTask() {
        this.billHeadFor = null;
    }

    public BillHeadLoadTask(String billHeadFor) {
        this.billHeadFor = billHeadFor;
    }

    @Override
    protected List<BillHead> call() throws Exception {
        try {
            BillHeadService service = EmcsAppContext.getContext().getBean(BillHeadService.class);
            List<BillHead> list = service.findAll();
            if (list == null || list.isEmpty()) {
                return null;
            }

            if (billHeadFor != null) {
                return list.stream().filter(bh -> billHeadFor.equals(bh.getBillHeadFor())).collect(Collectors.toList());
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("BillHead fetch", e);
        }
        return null;
    }
}
