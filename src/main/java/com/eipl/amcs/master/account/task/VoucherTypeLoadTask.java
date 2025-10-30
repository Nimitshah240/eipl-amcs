package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VoucherTypeLoadTask extends Task<List<VoucherType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherTypeLoadTask.class);

    @Override
    protected List<VoucherType> call() throws Exception {
        try {
            VoucherTypeService service = EmcsAppContext.getContext().getBean(VoucherTypeService.class);
            List<VoucherType> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("VoucherType fetch", e);
        }
        return null;
    }
}
