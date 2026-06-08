package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Vendor;
import com.eipl.amcs.master.operation.service.VendorService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VendorLoadTask extends Task<List<Vendor>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorLoadTask.class);

    @Override
    protected List<Vendor> call() throws Exception {
        try {
            VendorService service = EmcsAppContext.getContext().getBean(VendorService.class);
            List<Vendor> list = service.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Vendor fetch", e);
        }
        return null;
    }
}